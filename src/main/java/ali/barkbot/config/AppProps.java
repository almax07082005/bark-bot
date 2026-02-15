package ali.barkbot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "app")
@Configuration
public class AppProps {

    private String botToken;
    private Integer threadPoolSize;
    private Long threadTimeoutMs;
}
