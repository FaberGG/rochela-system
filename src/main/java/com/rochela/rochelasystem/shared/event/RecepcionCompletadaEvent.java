package com.rochela.rochelasystem.shared.event;

import lombok.Getter;

@Getter
public class RecepcionCompletadaEvent {

    private final Long recepcionId;

    public RecepcionCompletadaEvent(Long recepcionId) {
        this.recepcionId = recepcionId;
    }
}
