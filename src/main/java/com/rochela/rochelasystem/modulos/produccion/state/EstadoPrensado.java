package com.rochela.rochelasystem.modulos.produccion.state;

import com.rochela.rochelasystem.shared.enums.EstadoLote;

public class EstadoPrensado implements LoteState {

    private final StateResolver resolver;

    public EstadoPrensado(StateResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public EstadoLote getEstado() {
        return EstadoLote.PRENSADO;
    }

    /**
     * El prensado NO avanza automáticamente al guardar la etapa.
     * La transición a FINALIZADO la ejecuta cerrarLote() explícitamente,
     * porque el cierre requiere datos adicionales (unidades, peso, rendimientos).
     */
    @Override
    public LoteState avanzar(LoteStateContext context) {
        throw new IllegalStateException(
                "El prensado no avanza automáticamente. Use el endpoint /cierre para finalizar el lote.");
    }

    /**
     * Informa al frontend que el siguiente paso es el cierre del lote (FINALIZADO),
     * sin ejecutar la transición.
     */
    @Override
    public LoteState siguiente(LoteStateContext context) {
        return resolver.resolve(EstadoLote.FINALIZADO);
    }

    @Override
    public LoteState cancelar(LoteStateContext context) {
        return resolver.resolve(EstadoLote.CANCELADO);
    }
}