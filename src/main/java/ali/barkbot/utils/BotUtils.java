package ali.barkbot.utils;

import ali.barkbot.constants.Buttons;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class BotUtils {

    public static SendMessage sendMessageWithSupportButton(Long chatId, String text,
                                                           String supportUsername, String messageTemplate) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        String encodedMessage = URLEncoder.encode(messageTemplate, StandardCharsets.UTF_8)
                .replace("+", "%20");
        String supportUrl = String.format("https://t.me/%s?text=%s", supportUsername, encodedMessage);

        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton(Buttons.CONTACT_SUPPORT_TEXT).url(supportUrl)
        );

        return new SendMessage(chatId, text)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup);
    }
}
