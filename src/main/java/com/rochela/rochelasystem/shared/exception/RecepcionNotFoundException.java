package com.rochela.rochelasystem.shared.exception;

import org.springframework.http.HttpStatus;

public class RecepcionNotFoundException extends LacteaException {

    public RecepcionNotFoundException(Long recepcionId) {
        super("Recepcion de leche no encontrada: " + recepcionId, HttpStatus.NOT_FOUND, "RECEPCION_NO_ENCONTRADA");
    }

    public RecepcionNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "RECEPCION_NO_ENCONTRADA");
    }
}

