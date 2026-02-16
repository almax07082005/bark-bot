package ali.barkbot.command.handlers;

import ali.barkbot.command.CommandHandler;
import ali.barkbot.config.AppProps;
import ali.barkbot.constants.Commands;
import ali.barkbot.constants.Messages;
import ali.barkbot.entity.model.CameFrom;
import ali.barkbot.mapper.UserMapper;
import ali.barkbot.service.UserService;
import ali.barkbot.utils.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component(Commands.START)
// FIXME handle different cameFrom with text after /start command
public class StartCommandHandler implements CommandHandler {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AppProps appProps;

    @Override
    public void handle(Update update, TelegramBot telegramBot) {
        User user = update.message().from();
        Long pid = user.id();
        String username = user.username();

        userService.save(userMapper.toEntity(user, CameFrom.Other));
        telegramBot.execute(BotUtils.sendMessageWithSupportButton(pid, Messages.START_MESSAGE,
                appProps.getSupportUsername(), appProps.getSupportMessageTemplate()));

        log.info("Sent {} message to user `{}` with pid `{}`", Commands.START, username, pid);
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
