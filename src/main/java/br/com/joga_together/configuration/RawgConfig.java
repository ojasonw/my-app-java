package br.com.joga_together.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RawgConfig {

    @Value("${rawg.base-url:https://api.rawg.io/api}")
    private String baseUrl;

    @Bean(name = "rawgRestClient")
    public RestClient rawgRestClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
