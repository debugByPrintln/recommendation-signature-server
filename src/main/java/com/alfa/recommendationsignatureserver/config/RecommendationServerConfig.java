package com.alfa.recommendationsignatureserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RecommendationServerConfig {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
