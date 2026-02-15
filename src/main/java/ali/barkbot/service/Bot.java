package ali.barkbot.service;

import ali.barkbot.common.Constants;
import ali.barkbot.config.AppProps;
import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
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
public class Bot {

    private final AppProps appProps;
    private final ExecutorService executorService;
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
                        .runAsync(() -> handleUpdate(update), executorService)
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

    private void handleUpdate(Update update) {
        telegramBot.execute(new SendMessage(update.message().from().id(), Constants.START_MESSAGE));
        log.info("Sent start message to user `{}` with id `{}`",
                update.message().from().username(),
                update.message().from().id());
    }
}
