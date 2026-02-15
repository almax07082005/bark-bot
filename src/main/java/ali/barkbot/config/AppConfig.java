package ali.barkbot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
@Configuration
public class AppConfig {

    private final AppProps appProps;

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(appProps.getThreadPoolSize());
    }
}
