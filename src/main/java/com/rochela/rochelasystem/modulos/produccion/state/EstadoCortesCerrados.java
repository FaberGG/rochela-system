package com.rochela.rochelasystem.modulos.produccion.state;

import com.rochela.rochelasystem.shared.enums.EstadoLote;

public class EstadoCortesCerrados implements LoteState {

    private final StateResolver resolver;

    public EstadoCortesCerrados(StateResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public EstadoLote getEstado() {
        return EstadoLote.CORTES_CERRADOS;
    }

    @Override
    public LoteState avanzar(LoteStateContext context) {
        if (context.requiereLavadoDesuerado()) {
            return resolver.resolve(EstadoLote.LAVADO_DESUERADO);
        }
        return resolver.resolve(EstadoLote.DESUERADO);
    }

    @Override
    public LoteState cancelar(LoteStateContext context) {
        return resolver.resolve(EstadoLote.CANCELADO);
    }
}
