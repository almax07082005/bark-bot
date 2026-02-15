package ali.barkbot.command;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class CommandRegistry {

    private final Map<String, CommandHandler> commandMap;

    @PostConstruct
    public void init() {
        log.info("Registered {} command handlers: {}",
                commandMap.size(),
                commandMap.keySet());
    }

    public Optional<CommandHandler> getHandler(String command) {
        if (command == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(commandMap.get(command));
    }
}
