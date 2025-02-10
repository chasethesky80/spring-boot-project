package se.magnus.microservices.composite.product;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import se.magnus.util.http.ServiceUtil;

@Configuration
public class BeanConfiguration {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
