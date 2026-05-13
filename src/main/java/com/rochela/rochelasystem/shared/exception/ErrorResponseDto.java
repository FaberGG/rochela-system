package com.rochela.rochelasystem.shared.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(name = "ErrorResponse", description = "Estructura estandar de error")
public class ErrorResponseDto {

    @Schema(description = "Fecha y hora del error", example = "2026-05-13T12:00:00")
    private final LocalDateTime timestamp;

    @Schema(description = "Codigo HTTP", example = "404")
    private final int status;

    @Schema(description = "Nombre del error", example = "Not Found")
    private final String error;

    @Schema(description = "Mensaje de error", example = "Recurso no encontrado")
    private final String message;

    @Schema(description = "Ruta solicitada", example = "/api/v1/lotes/99")
    private final String path;

    @Schema(description = "Codigo interno de error", example = "RECURSO_NO_ENCONTRADO")
    private final String code;

    public ErrorResponseDto(LocalDateTime timestamp,
                            int status,
                            String error,
                            String message,
                            String path,
                            String code) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.code = code;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public String getCode() {
        return code;
    }
}

