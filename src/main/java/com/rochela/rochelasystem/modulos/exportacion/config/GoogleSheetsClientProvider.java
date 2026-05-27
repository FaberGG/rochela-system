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
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoogleSheetsClientProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSheetsClientProvider.class);
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final String credentialsBase64;
    private final String credentialsPath;
    private final String applicationName;

    public GoogleSheetsClientProvider(String credentialsBase64,
                                      String credentialsPath,
                                      String applicationName) {
        this.credentialsBase64 = credentialsBase64;
        this.credentialsPath = credentialsPath;
        this.applicationName = applicationName;
    }

    public Optional<Sheets> getSheets() {
        try (InputStream inputStream = resolveCredentialsStream()) {
            GoogleCredential credential = GoogleCredential.fromStream(inputStream)
                    .createScoped(List.of(SheetsScopes.SPREADSHEETS));
            Sheets sheets = new Sheets.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JSON_FACTORY,
                    credential)
                    .setApplicationName(applicationName)
                    .build();
            return Optional.of(sheets);
        } catch (IllegalStateException ex) {
            LOGGER.warn("{} Se omitira la sincronizacion.", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOGGER.warn("Las credenciales de Google Sheets en base64 no son validas. Se omitira la sincronizacion.");
        } catch (IOException | GeneralSecurityException ex) {
            LOGGER.error("No se pudo inicializar el cliente de Google Sheets. Se omitira la sincronizacion.", ex);
        }
        return Optional.empty();
    }

    private InputStream resolveCredentialsStream() throws IOException {
        if (credentialsBase64 != null && !credentialsBase64.isBlank()) {
            String sanitized = credentialsBase64.replaceAll("\\s", "");
            byte[] decodedBytes = Base64.getDecoder().decode(sanitized);
            return new ByteArrayInputStream(decodedBytes);
        }
        if (credentialsPath != null && !credentialsPath.isBlank()) {
            Path path = Path.of(credentialsPath);
            if (!Files.exists(path)) {
                throw new IllegalStateException("No se encontro el archivo de credenciales de Google Sheets en " + credentialsPath + ".");
            }
            return Files.newInputStream(path);
        }
        throw new IllegalStateException("Se requiere exportacion.google.credentials-base64 o exportacion.google.credentials-path.");
    }
}
