package com.rochela.rochelasystem.shared.exception;

import org.springframework.http.HttpStatus;

public class EstadoInvalidoException extends LacteaException {

    public EstadoInvalidoException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "ESTADO_INVALIDO");
    }
}

