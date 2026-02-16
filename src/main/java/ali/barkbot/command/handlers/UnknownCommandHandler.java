package ali.barkbot.command.handlers;

import ali.barkbot.command.CommandHandler;
import ali.barkbot.constants.Commands;
import ali.barkbot.constants.Messages;
import ali.barkbot.utils.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component(Commands.UNKNOWN)
public class UnknownCommandHandler implements CommandHandler {

    private final List<CommandHandler> commandHandlers;

    @Override
    public void handle(Update update, TelegramBot telegramBot) {
        Long pid = update.message().from().id();
        String username = update.message().from().username();

        String availableCommands = commandHandlers.stream()
                .filter(handler -> !(handler instanceof UnknownCommandHandler))
                .map(handler -> handler.getCommand() + " — " + handler.getDescription())
                .collect(Collectors.joining("\n"));
        String message = Messages.UNKNOWN_COMMAND_MESSAGE.formatted(availableCommands);

        telegramBot.execute(BotUtils.sendMessage(pid, message));
        log.info("Sent {} message to user `{}` with id `{}`", Commands.UNKNOWN, username, pid);
    }

    @Override
    public String getCommand() {
        return Commands.UNKNOWN;
    }

    @Override
    public String getDescription() {
        return "Default handler for unknown commands";
    }
}
