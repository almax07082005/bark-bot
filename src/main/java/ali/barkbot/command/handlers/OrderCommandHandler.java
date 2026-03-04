package ali.barkbot.command.handlers;

import ali.barkbot.command.CommandHandler;
import ali.barkbot.constants.Commands;
import ali.barkbot.service.OrderFlowService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component(Commands.ORDER)
public class OrderCommandHandler implements CommandHandler {

    private final OrderFlowService orderFlowService;

    @Override
    public void handle(Update update, TelegramBot telegramBot) {
        Long chatId = update.message().from().id();
        orderFlowService.start(chatId, telegramBot);
        log.info("Sent {} message to user `{}` with id `{}`", Commands.ORDER, update.message().from().username(), chatId);
    }

    @Override
    public String getCommand() {
        return Commands.ORDER;
    }

    @Override
    public String getDescription() {
        return "Быстрый заказ";
    }
}
