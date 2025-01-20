package io.ankush.kap_mini.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ReferrerPolicyConfig {

    @Bean
    public Filter referrerPolicyFilter() {
        return (ServletRequest request, ServletResponse response, FilterChain chain) -> {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
            chain.doFilter(request, response);
        };
    }
}