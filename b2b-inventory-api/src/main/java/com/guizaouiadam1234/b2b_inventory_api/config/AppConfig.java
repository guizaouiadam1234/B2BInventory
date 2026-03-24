package com.guizaouiadam1234.b2b_inventory_api.config;

import com.guizaouiadam1234.b2b_inventory_api.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Value("${auth.api.url:http://localhost:8080}")
    private String authApiUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter(RestTemplate restTemplate) {
        return new JwtAuthFilter(restTemplate, authApiUrl);
    }
}
