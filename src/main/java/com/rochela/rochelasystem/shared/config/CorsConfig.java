package com.rochela.rochelasystem.shared.config;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final String[] allowedOrigins;
    private final String[] allowedMethods;
    private final String[] allowedHeaders;
    private final boolean allowCredentials;
    private final long maxAge;

    public CorsConfig(@Value("${app.cors.allowed-origins:}") String allowedOrigins,
                      @Value("${app.cors.allowed-methods:GET,POST,PUT,DELETE,PATCH,OPTIONS}") String allowedMethods,
                      @Value("${app.cors.allowed-headers:*}") String allowedHeaders,
                      @Value("${app.cors.allow-credentials:true}") boolean allowCredentials,
                      @Value("${app.cors.max-age:3600}") long maxAge) {
        this.allowedOrigins = parseCsv(allowedOrigins);
        this.allowedMethods = parseCsv(allowedMethods);
        this.allowedHeaders = parseCsv(allowedHeaders);
        this.allowCredentials = allowCredentials;
        this.maxAge = maxAge;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (allowedOrigins.length == 0) {
            return;
        }

        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods(allowedMethods)
                .allowedHeaders(allowedHeaders)
                .allowCredentials(allowCredentials)
                .maxAge(maxAge);
    }

    private String[] parseCsv(String value) {
        if (value == null || value.isBlank()) {
            return new String[0];
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(item -> !item.isEmpty())
                .toArray(String[]::new);
    }
}
