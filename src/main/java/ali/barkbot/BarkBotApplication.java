package ali.barkbot;

import ali.barkbot.service.BotService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BarkBotApplication {
    static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BarkBotApplication.class, args);
        context.getBean(BotService.class).startBot();
    }
}
