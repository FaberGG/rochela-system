package com.rochela.rochelasystem.modulos.produccion.state;

import com.rochela.rochelasystem.shared.enums.EstadoLote;
import java.util.EnumMap;
import java.util.Map;

public class StateResolver {

    private final Map<EstadoLote, LoteState> states = new EnumMap<>(EstadoLote.class);

    public StateResolver() {
        states.put(EstadoLote.INICIADO, new EstadoIniciado(this));
        states.put(EstadoLote.PASTEURIZACION, new EstadoPasteurizacion(this));
        states.put(EstadoLote.CLORURO, new EstadoCloruro(this));
        states.put(EstadoLote.CUAJO, new EstadoCuajo(this));
        states.put(EstadoLote.CORTES, new EstadoCortes(this));
        states.put(EstadoLote.LAVADO_DESUERADO, new EstadoLavadoDesuerado(this));
        states.put(EstadoLote.DESUERADO, new EstadoDesuerado(this));
        states.put(EstadoLote.SALADO, new EstadoSalado(this));
        states.put(EstadoLote.PRENSADO, new EstadoPrensado(this));
        states.put(EstadoLote.FINALIZADO, new EstadoFinalizado(this));
        states.put(EstadoLote.CANCELADO, new EstadoCancelado(this));
    }

    public LoteState resolve(EstadoLote estado) {
        LoteState state = states.get(estado);
        if (state == null) {
            throw new IllegalArgumentException("Estado no soportado: " + estado);
        }
        return state;
    }
}

