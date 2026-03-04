package ali.barkbot.service;

import ali.barkbot.config.AppProps;
import ali.barkbot.constants.Buttons;
import ali.barkbot.constants.Messages;
import ali.barkbot.dto.DeliveryPayloadDto;
import ali.barkbot.dto.DeliverySelectionDto;
import ali.barkbot.dto.EditPayloadDto;
import ali.barkbot.dto.FractionPayloadDto;
import ali.barkbot.dto.QuantityEntryDto;
import ali.barkbot.entity.OrderCallbackButtonEntity;
import ali.barkbot.entity.OrderSessionEntity;
import ali.barkbot.mapper.OrderSessionMapper;
import ali.barkbot.model.OrderStep;
import ali.barkbot.utils.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderFlowService {

    private static final Pattern NUMERIC = Pattern.compile("\\d+");
    private static final int MIN_QTY = 1;
    private static final int MAX_QTY = 999;
    private static final int MIN_ADDRESS_LEN = 5;
    private static final int MAX_ADDRESS_LEN = 500;

    private final OrderSessionService sessionService;
    private final OrderCallbackButtonService callbackButtonService;
    private final OrderSessionMapper orderSessionMapper;
    private final AppProps appProps;

    public boolean hasActiveSession(Long chatId) {
        return sessionService.hasActiveSession(chatId);
    }

    public void start(Long chatId, TelegramBot bot) {
        sessionService.start(chatId);
        bot.execute(BotUtils.sendMessageWithKeyboardMarkup(chatId, Messages.ORDER_CHOOSE_FRACTION,
                callbackButtonService.buildFractionKeyboard()));
    }

    public void handleCallback(Update update, TelegramBot bot) {
        CallbackQuery cq = update.callbackQuery();
        if (cq == null) {
            return;
        }

        String callbackId = cq.id();
        Long chatId = cq.message().chat().id();
        String buttonId = cq.data();

        bot.execute(new AnswerCallbackQuery(callbackId));
        if (buttonId == null || buttonId.isBlank()) {
            return;
        }


        OrderCallbackButtonEntity button = callbackButtonService.findById(buttonId);
        if (button == null) {
            log.warn("Unknown callback button id: {}", buttonId);
            return;
        }

        log.info("Pressed button {} ({}) by user `{}` with id `{}`", buttonId, button.getButtonType(),
                cq.from().username(), chatId);

        OrderSessionEntity session = sessionService.get(chatId);
        if (session == null) {
            log.warn("Callback {} for chatId `{}` but no active session", buttonId, chatId);
            return;
        }

        switch (button.getButtonType()) {
            case CANCEL -> handleCancel(chatId, bot);
            case FRACTION -> handleFraction(chatId, session, button, bot);
            case DELIVERY -> handleDelivery(chatId, session, button, bot);
            case EDIT -> handleEdit(chatId, session, button, bot);
        }
    }

    private void handleCancel(Long chatId, TelegramBot bot) {
        sessionService.remove(chatId);
        bot.execute(BotUtils.sendMessage(chatId, Messages.ORDER_CANCELLED));
    }

    private void handleFraction(Long chatId, OrderSessionEntity session, OrderCallbackButtonEntity button, TelegramBot bot) {
        FractionPayloadDto payload = callbackButtonService.toFractionPayload(button);
        orderSessionMapper.withFractionSelected(session, payload);
        sessionService.update(session);

        String text = Messages.ORDER_FRACTION_CONFIRMED.formatted(payload.frac(), payload.name());
        bot.execute(BotUtils.sendMessage(chatId, text));
        bot.execute(BotUtils.sendMessage(chatId, Messages.ORDER_ASK_QUANTITY));
    }

    private void handleDelivery(Long chatId, OrderSessionEntity session, OrderCallbackButtonEntity button, TelegramBot bot) {
        DeliveryPayloadDto payload = callbackButtonService.toDeliveryPayload(button);
        boolean isPickup = Buttons.DELIVERY_METHOD_PICKUP.equals(payload.method());

        OrderStep nextStep = isPickup ? OrderStep.CONFIRMATION : OrderStep.ADDRESS;
        String address = isPickup ? Messages.ORDER_ADDRESS_NONE : null;

        DeliverySelectionDto selection = new DeliverySelectionDto(nextStep, payload.method(), address);
        orderSessionMapper.withDeliverySelected(session, selection);
        sessionService.update(session);
        if (isPickup) {
            showConfirmation(chatId, session, bot);
        } else {
            bot.execute(BotUtils.sendMessage(chatId, Messages.ORDER_ASK_ADDRESS));
        }
    }

    private void handleEdit(Long chatId, OrderSessionEntity session, OrderCallbackButtonEntity button, TelegramBot bot) {
        EditPayloadDto payload = callbackButtonService.toEditPayload(button);
        orderSessionMapper.withStep(session, payload.target());
        sessionService.update(session);

        switch (payload.target()) {
            case EDIT_MENU -> bot.execute(BotUtils.sendMessageWithKeyboardMarkup(chatId, Messages.ORDER_EDIT_MENU,
                    callbackButtonService.buildEditKeyboard()));
            case FRACTION -> bot.execute(BotUtils.sendMessageWithKeyboardMarkup(chatId, Messages.ORDER_CHOOSE_FRACTION,
                    callbackButtonService.buildFractionKeyboard()));
            case QUANTITY -> bot.execute(BotUtils.sendMessage(chatId, Messages.ORDER_ASK_QUANTITY));
            case DELIVERY_METHOD ->
                    bot.execute(BotUtils.sendMessageWithKeyboardMarkup(chatId, Messages.ORDER_ASK_DELIVERY,
                            callbackButtonService.buildDeliveryKeyboard()));
            case CONFIRMATION -> showConfirmation(chatId, session, bot);
            default -> log.warn("Unknown edit target: {}", payload.target());
        }
    }

    public void handleText(Update update, TelegramBot bot) {
        if (update.message() == null || update.message().text() == null) {
            return;
        }
        Long chatId = update.message().from().id();
        String text = update.message().text();

        OrderSessionEntity session = sessionService.get(chatId);
        if (session == null) {
            return;
        }

        log.info("Entered text for step {} by user `{}` with id `{}`", session.getStep(),
                update.message().from().username(), chatId);

        switch (session.getStep()) {
            case QUANTITY -> {
                if (!NUMERIC.matcher(text).matches()) {
                    bot.execute(BotUtils.sendMessage(chatId, Messages.ORDER_QUANTITY_ERROR_NOT_NUMBER));
                    return;
                }

                int qty = Integer.parseInt(text);
                if (qty < MIN_QTY || qty > MAX_QTY) {
                    bot.execute(BotUtils.sendMessage(chatId, Messages.ORDER_QUANTITY_ERROR_RANGE));
                    return;
                }
                int qtyFinal = (int) Math.ceil(qty * 1.1);

                QuantityEntryDto quantityEntry = new QuantityEntryDto(qty, qtyFinal);
                orderSessionMapper.withQuantityEntered(session, quantityEntry);
                sessionService.update(session);

                String confirmed = Messages.ORDER_QUANTITY_CONFIRMED.formatted(qty, qtyFinal);
                bot.execute(BotUtils.sendMessage(chatId, confirmed));
                bot.execute(BotUtils.sendMessageWithKeyboardMarkup(chatId, Messages.ORDER_ASK_DELIVERY,
                        callbackButtonService.buildDeliveryKeyboard()));
            }
            case ADDRESS -> {
                int addressLen = text.length();
                if (addressLen < MIN_ADDRESS_LEN || addressLen > MAX_ADDRESS_LEN) {
                    bot.execute(BotUtils.sendMessage(chatId, Messages.ORDER_ADDRESS_ERROR));
                    return;
                }
                orderSessionMapper.withAddressEntered(session, text);
                sessionService.update(session);
                showConfirmation(chatId, session, bot);
            }
        }
    }

    private void showConfirmation(Long chatId, OrderSessionEntity session, TelegramBot bot) {
        String body = Messages.ORDER_CONFIRM_BODY.formatted(
                session.getFrac(),
                session.getName(),
                session.getQtyFinal(),
                session.getQtyUser(),
                session.getMethod(),
                session.getAddress() != null ? session.getAddress() : Messages.ORDER_ADDRESS_NONE
        );
        String fullText = Messages.ORDER_CONFIRM_HEADER + "\n\n" + body + "\n\n" + Messages.ORDER_CONFIRM_FOOTER;
        String encoded = BotUtils.encodeOrderTextForDeepLink(body);

        String url = "https://t.me/" + appProps.getSupportUsername() + "?text=" + encoded;
        bot.execute(BotUtils.sendMessageWithKeyboardMarkup(chatId, fullText,
                callbackButtonService.buildConfirmationKeyboard(url)));
    }
}
