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
@Component(Commands.FAQ)
// TODO faq must be implemented with buttons
public class FaqCommandHandler implements CommandHandler {

    @Override
    public void handle(Update update, TelegramBot telegramBot) {
        Long pid = update.message().from().id();
        String username = update.message().from().username();

        telegramBot.execute(BotUtils.sendMessage(pid, Messages.FAQ_MESSAGE));
        log.info("Sent {} message to user `{}` with id `{}`", Commands.FAQ, username, pid);
    }

    @Override
    public String getCommand() {
        return Commands.FAQ;
    }

    @Override
    public String getDescription() {
        return "Посмотреть базу знаний";
    }
}
