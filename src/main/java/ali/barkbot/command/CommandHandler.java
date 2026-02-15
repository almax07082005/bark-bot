package ali.barkbot.command;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

public interface CommandHandler {

    void handle(Update update, TelegramBot telegramBot);

    String getCommand();

    String getDescription();
}
