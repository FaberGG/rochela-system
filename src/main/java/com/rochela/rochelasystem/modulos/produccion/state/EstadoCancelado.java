package com.rochela.rochelasystem.modulos.produccion.state;

import com.rochela.rochelasystem.shared.enums.EstadoLote;

public class EstadoCancelado implements LoteState {

    private final StateResolver resolver;

    public EstadoCancelado(StateResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public EstadoLote getEstado() {
        return EstadoLote.CANCELADO;
    }

    @Override
    public LoteState avanzar(LoteStateContext context) {
        throw new IllegalStateException("No se puede avanzar desde CANCELADO.");
    }

    @Override
    public LoteState cancelar(LoteStateContext context) {
        throw new IllegalStateException("El lote ya esta CANCELADO.");
    }
}

