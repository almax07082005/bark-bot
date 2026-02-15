package ali.barkbot.utils;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

public final class BotUtils {

    public static SendMessage sendMessage(long chatId, String text) {
        return new SendMessage(chatId, text).parseMode(ParseMode.Markdown);
    }
}
