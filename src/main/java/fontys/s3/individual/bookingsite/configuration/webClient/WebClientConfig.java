package fontys.s3.individual.bookingsite.configuration.webClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig
{
    @Bean
    public WebClient webClient() {
        return WebClient.create(); // You can configure WebClient options here if needed
    }
}
