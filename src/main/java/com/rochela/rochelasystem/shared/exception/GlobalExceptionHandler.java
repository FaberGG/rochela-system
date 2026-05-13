package com.rochela.rochelasystem.shared.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LacteaException.class)
    public ResponseEntity<ErrorResponseDto> handleLacteaException(
            LacteaException exception,
            HttpServletRequest request) {
        ErrorResponseDto response = new ErrorResponseDto(
                LocalDateTime.now(),
                exception.getStatus().value(),
                exception.getStatus().getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI(),
                exception.getErrorCode()
        );
        return ResponseEntity.status(exception.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleUnexpectedException(
            Exception exception,
            HttpServletRequest request) {
        ErrorResponseDto response = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Error inesperado",
                request.getRequestURI(),
                "ERROR_INTERNO"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

