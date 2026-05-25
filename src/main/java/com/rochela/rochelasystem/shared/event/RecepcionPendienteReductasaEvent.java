package com.rochela.rochelasystem.shared.event;

import lombok.Getter;

@Getter
public class RecepcionPendienteReductasaEvent {

    private final Long recepcionId;

    public RecepcionPendienteReductasaEvent(Long recepcionId) {
        this.recepcionId = recepcionId;
    }
}
