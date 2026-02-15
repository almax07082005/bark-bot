package ali.barkbot.command;

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

    public void route(Update update, TelegramBot telegramBot) {
        String command = extractCommand(update);

        if (command != null) {
            commandRegistry.getHandler(command)
                    .ifPresentOrElse(
                            handler -> {
                                log.debug("Routing command {} to handler {} (bean: {})",
                                        command, handler.getClass().getSimpleName(), handler.getCommand());
                                handler.handle(update, telegramBot);
                            },
                            () -> {
                                log.debug("Unknown command: {}", command);
                                unknown.handle(update, telegramBot);
                            }
                    );
        } else {
            log.debug("No command found in message, using unknown handler");
            unknown.handle(update, telegramBot);
        }
    }

    private String extractCommand(Update update) {
        if (update.message() == null || update.message().text() == null) {
            return null;
        }

        String text = update.message().text().trim();
        if (!text.startsWith("/")) {
            return null;
        }

        return text;
    }
}
