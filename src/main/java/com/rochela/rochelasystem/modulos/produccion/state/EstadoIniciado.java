package com.rochela.rochelasystem.modulos.produccion.state;

import com.rochela.rochelasystem.shared.enums.EstadoLote;

public class EstadoIniciado implements LoteState {

    private final StateResolver resolver;

    public EstadoIniciado(StateResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public EstadoLote getEstado() {
        return EstadoLote.INICIADO;
    }

    @Override
    public LoteState avanzar(LoteStateContext context) {
        if (context.requierePasteurizacion()) {
            return resolver.resolve(EstadoLote.PASTEURIZACION);
        }
        if (context.requiereCloruro()) {
            return resolver.resolve(EstadoLote.CLORURO);
        }
        return resolver.resolve(EstadoLote.CUAJO);
    }

    @Override
    public LoteState cancelar(LoteStateContext context) {
        return resolver.resolve(EstadoLote.CANCELADO);
    }
}

