package com.rochela.rochelasystem.modulos.produccion.state;

import com.rochela.rochelasystem.shared.enums.EstadoLote;

public class EstadoPrensadoIniciado implements LoteState {

    private final StateResolver resolver;

    public EstadoPrensadoIniciado(StateResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public EstadoLote getEstado() {
        return EstadoLote.PRENSADO_INICIADO;
    }

    @Override
    public LoteState avanzar(LoteStateContext context) {
        return resolver.resolve(EstadoLote.PRENSADO);
    }

    @Override
    public LoteState siguiente(LoteStateContext context) {
        return resolver.resolve(EstadoLote.PRENSADO);
    }

    @Override
    public LoteState cancelar(LoteStateContext context) {
        return resolver.resolve(EstadoLote.CANCELADO);
    }
}

