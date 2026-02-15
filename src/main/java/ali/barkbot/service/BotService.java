package ali.barkbot.service;

import ali.barkbot.command.CommandRouter;
import ali.barkbot.config.AppProps;
import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class BotService {

    private final AppProps appProps;
    private final ExecutorService executorService;
    private final CommandRouter commandRouter;
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot = new TelegramBot(appProps.getBotToken());
    }

    public void startBot() {
        telegramBot.setUpdatesListener(updatesListener(), getExceptionHandler());
    }

    private ExceptionHandler getExceptionHandler() {
        return exception -> {
            if (exception.response() != null) {
                log.error("Telegram API error with code {}: {}",
                        exception.response().errorCode(),
                        exception.response().description());
            } else {
                log.error("Unexpected error", exception);
            }
        };
    }

    private UpdatesListener updatesListener() {
        return updates -> {
            List<CompletableFuture<Void>> futures = new ArrayList<>();

            for (Update update : updates) {
                futures.add(CompletableFuture
                        .runAsync(() -> commandRouter.route(update, telegramBot), executorService)
                        .orTimeout(appProps.getThreadTimeoutMs(), TimeUnit.MILLISECONDS)
                        .handleAsync((result, exception) -> {
                            if (exception != null) {
                                log.error("Error processing update for username {}, id: {}",
                                        update.message().from().username(),
                                        update.message().from().id(),
                                        exception);
                            }
                            return result;
                        }));
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            }

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        };
    }
}
