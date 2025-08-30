package com.taufer.tales.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    WebClient openLibraryWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://openlibrary.org")
                .build();
    }
}