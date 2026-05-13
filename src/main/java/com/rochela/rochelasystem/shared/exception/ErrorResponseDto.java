package com.rochela.rochelasystem.shared.exception;

import java.time.LocalDateTime;

public class ErrorResponseDto {

    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
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

