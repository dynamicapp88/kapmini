package io.ankush.kap_mini.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors().disable() // Completely disable CORS
            .csrf().disable() // Optional: Disable CSRF for testing purposes
            .authorizeRequests()
            .anyRequest().permitAll(); // Allow all requests
        return http.build();
    }
}