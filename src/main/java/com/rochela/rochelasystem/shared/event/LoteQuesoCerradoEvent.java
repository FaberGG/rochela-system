package com.rochela.rochelasystem.shared.event;

import lombok.Getter;

@Getter
public class LoteQuesoCerradoEvent {

    private final Long loteQuesoId;

    public LoteQuesoCerradoEvent(Long loteQuesoId) {
        this.loteQuesoId = loteQuesoId;
    }
}
