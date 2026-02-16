package ali.barkbot.command.handlers;

import ali.barkbot.command.CommandHandler;
import ali.barkbot.constants.Commands;
import ali.barkbot.constants.Messages;
import ali.barkbot.utils.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component(Commands.START)
public class StartCommandHandler implements CommandHandler {

    @Override
    public void handle(Update update, TelegramBot telegramBot) {
        Long userId = update.message().from().id();
        String username = update.message().from().username();

        telegramBot.execute(BotUtils.sendMessage(userId, Messages.START_MESSAGE));
        log.info("Sent start message to user `{}` with id `{}`", username, userId);
    }

    @Override
    public String getCommand() {
        return Commands.START;
    }

    @Override
    public String getDescription() {
        return "Перейти в начало";
    }
}
