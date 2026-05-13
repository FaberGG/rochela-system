package com.rochela.rochelasystem.modulos.produccion.state;

import com.rochela.rochelasystem.shared.enums.EstadoLote;

public class EstadoCloruro implements LoteState {

    private final StateResolver resolver;

    public EstadoCloruro(StateResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public EstadoLote getEstado() {
        return EstadoLote.CLORURO;
    }

    @Override
    public LoteState avanzar(LoteStateContext context) {
        return resolver.resolve(EstadoLote.CUAJO);
    }

    @Override
    public LoteState cancelar(LoteStateContext context) {
        return resolver.resolve(EstadoLote.CANCELADO);
    }
}

