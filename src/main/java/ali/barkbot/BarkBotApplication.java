package ali.barkbot;

import ali.barkbot.service.Bot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BarkBotApplication {
    static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BarkBotApplication.class, args);
        context.getBean(Bot.class).startBot();
    }
}
