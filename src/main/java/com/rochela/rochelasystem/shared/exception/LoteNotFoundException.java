package com.rochela.rochelasystem.shared.exception;

import org.springframework.http.HttpStatus;

public class LoteNotFoundException extends LacteaException {

    public LoteNotFoundException(Long loteId) {
        super("Lote no encontrado: " + loteId, HttpStatus.NOT_FOUND, "LOTE_NO_ENCONTRADO");
    }

    public LoteNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "LOTE_NO_ENCONTRADO");
    }
}

