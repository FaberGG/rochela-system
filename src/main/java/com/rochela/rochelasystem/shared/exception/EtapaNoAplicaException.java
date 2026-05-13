package com.rochela.rochelasystem.shared.exception;

import org.springframework.http.HttpStatus;

public class EtapaNoAplicaException extends LacteaException {

    public EtapaNoAplicaException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "ETAPA_NO_APLICA");
    }
}

