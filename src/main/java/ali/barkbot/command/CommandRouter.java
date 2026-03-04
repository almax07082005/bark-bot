package ali.barkbot.command;

import ali.barkbot.constants.Commands;
import ali.barkbot.model.CameFrom;
import ali.barkbot.model.CommandString;
import ali.barkbot.service.OrderFlowService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CommandRouter {

    private final CommandRegistry commandRegistry;
    private final CommandHandler unknown;
    private final OrderFlowService orderFlowService;

    public void route(Update update, TelegramBot telegramBot) {
        if (update.callbackQuery() != null) {
            orderFlowService.handleCallback(update, telegramBot);
            return;
        }
        if (update.message() == null || update.message().text() == null) {
            return;
        }

        String text = update.message().text();
        Long chatId = update.message().from().id();
        CommandString commandString = extractCommand(text);

        if (commandString != null) {
            commandRegistry.getHandler(commandString.command())
                    .ifPresentOrElse(
                            handler -> {
                                log.debug("Routing command {} to handler {} (bean: {})",
                                        commandString, handler.getClass().getSimpleName(), handler.getCommand());
                                handler.handle(update, telegramBot);
                            },
                            () -> {
                                log.debug("Unknown command: {}", commandString);
                                unknown.handle(update, telegramBot);
                            }
                    );
            return;
        }

        if (orderFlowService.hasActiveSession(chatId)) {
            orderFlowService.handleText(update, telegramBot);
            return;
        }

        log.debug("No command found in message, using unknown handler");
        unknown.handle(update, telegramBot);
    }

    private CommandString extractCommand(String text) {
        if (!text.startsWith("/")) {
            return null;
        }

        String[] split = text.split(" ");
        if ((!Commands.START.equals(split[0]) && split.length != 1) || split.length > 2) {
            return null;
        }
        if (split.length == 1) {
            return CommandString.builder()
                    .command(split[0])
                    .cameFrom(CameFrom.Other)
                    .build();
        }

        return CommandString.builder()
                .command(split[0])
                .cameFrom(CameFrom.fromString(split[1]))
                .build();
    }
}
