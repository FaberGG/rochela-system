package com.rochela.rochelasystem.modulos.exportacion.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleSheetsConfig {

    @Value("${exportacion.google.credentials-base64:}")
    private String credentialsBase64;

    @Value("${exportacion.google.credentials-path:}")
    private String credentialsPath;

    @Value("${exportacion.google.application-name:rochelasystem}")
    private String applicationName;

    @Bean
    public GoogleSheetsClientProvider googleSheetsClientProvider() {
        return new GoogleSheetsClientProvider(credentialsBase64, credentialsPath, applicationName);
    }
}
