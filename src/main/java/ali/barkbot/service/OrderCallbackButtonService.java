package ali.barkbot.service;

import ali.barkbot.dto.DeliveryPayloadDto;
import ali.barkbot.dto.EditPayloadDto;
import ali.barkbot.dto.FractionPayloadDto;
import ali.barkbot.entity.OrderCallbackButtonEntity;
import ali.barkbot.model.CallbackButtonType;
import ali.barkbot.repository.OrderCallbackButtonRepository;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderCallbackButtonService {

    private final OrderCallbackButtonRepository repository;

    public OrderCallbackButtonEntity findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public InlineKeyboardMarkup buildFractionKeyboard() {
        List<OrderCallbackButtonEntity> fractions = repository.findByButtonTypeOrderById(CallbackButtonType.FRACTION);
        OrderCallbackButtonEntity cancel = repository.findById("cancel").orElseThrow();

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        for (int i = 0; i < fractions.size(); i += 2) {
            OrderCallbackButtonEntity left = fractions.get(i);
            InlineKeyboardButton leftBtn = new InlineKeyboardButton(left.getLabel()).callbackData(left.getId());

            if (i + 1 < fractions.size()) {
                OrderCallbackButtonEntity right = fractions.get(i + 1);
                markup.addRow(leftBtn, new InlineKeyboardButton(right.getLabel()).callbackData(right.getId()));
            } else {
                markup.addRow(leftBtn);
            }
        }

        markup.addRow(new InlineKeyboardButton(cancel.getLabel()).callbackData(cancel.getId()));
        return markup;
    }

    public InlineKeyboardMarkup buildDeliveryKeyboard() {
        List<OrderCallbackButtonEntity> delivery = repository.findByButtonTypeOrderById(CallbackButtonType.DELIVERY);
        OrderCallbackButtonEntity cancel = repository.findById("cancel").orElseThrow();

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.addRow(
                new InlineKeyboardButton(delivery.get(0).getLabel()).callbackData(delivery.get(0).getId()),
                new InlineKeyboardButton(delivery.get(1).getLabel()).callbackData(delivery.get(1).getId())
        );
        markup.addRow(new InlineKeyboardButton(cancel.getLabel()).callbackData(cancel.getId()));

        return markup;
    }

    public InlineKeyboardMarkup buildEditKeyboard() {
        List<OrderCallbackButtonEntity> editButtons = List.of(
                repository.findById("e_frac").orElseThrow(),
                repository.findById("e_qty").orElseThrow(),
                repository.findById("e_del").orElseThrow(),
                repository.findById("e_back").orElseThrow()
        );

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        for (OrderCallbackButtonEntity btn : editButtons) {
            markup.addRow(new InlineKeyboardButton(btn.getLabel()).callbackData(btn.getId()));
        }

        return markup;
    }

    public InlineKeyboardMarkup buildConfirmationKeyboard(String managerDeepLinkUrl) {
        OrderCallbackButtonEntity editBtn = repository.findById("e_menu").orElseThrow();
        OrderCallbackButtonEntity cancel = repository.findById("cancel").orElseThrow();

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.addRow(new InlineKeyboardButton("💬 Отправить заказ менеджеру").url(managerDeepLinkUrl));
        markup.addRow(
                new InlineKeyboardButton(editBtn.getLabel()).callbackData(editBtn.getId()),
                new InlineKeyboardButton(cancel.getLabel()).callbackData(cancel.getId())
        );

        return markup;
    }

    public FractionPayloadDto toFractionPayload(OrderCallbackButtonEntity button) {
        return FractionPayloadDto.fromPayload(button.getPayloadJson());
    }

    public DeliveryPayloadDto toDeliveryPayload(OrderCallbackButtonEntity button) {
        return DeliveryPayloadDto.fromPayload(button.getPayloadJson());
    }

    public EditPayloadDto toEditPayload(OrderCallbackButtonEntity button) {
        return EditPayloadDto.fromPayload(button.getPayloadJson());
    }
}
