package com.rochela.rochelasystem.modulos.exportacion.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleSheetsConfig {

    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @Value("${exportacion.google.credentials-base64:}")
    private String credentialsBase64;

    @Value("${exportacion.google.credentials-path:}")
    private String credentialsPath;

    @Value("${exportacion.google.application-name:rochelasystem}")
    private String applicationName;

    @Bean
    public Sheets googleSheets() throws GeneralSecurityException, IOException {
        GoogleCredential credential;
        try (InputStream inputStream = resolveCredentialsStream()) {
            credential = GoogleCredential.fromStream(inputStream)
                    .createScoped(List.of(SheetsScopes.SPREADSHEETS));
        }

        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential)
                .setApplicationName(applicationName)
                .build();
    }

    private InputStream resolveCredentialsStream() throws IOException {
        if (credentialsBase64 != null && !credentialsBase64.isBlank()) {
            String sanitized = credentialsBase64.replaceAll("\\s", "");
            byte[] decodedBytes = Base64.getDecoder().decode(sanitized);
            return new ByteArrayInputStream(decodedBytes);
        }
        if (credentialsPath != null && !credentialsPath.isBlank()) {
            return Files.newInputStream(Path.of(credentialsPath));
        }
        throw new IllegalStateException("Se requiere exportacion.google.credentials-base64 o exportacion.google.credentials-path");
    }
}
