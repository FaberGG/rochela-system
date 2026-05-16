package com.rochela.rochelasystem.modulos.produccion.state;

import com.rochela.rochelasystem.shared.enums.EstadoLote;

public interface LoteState {

    EstadoLote getEstado();

    /**
     * Ejecuta la transición real al siguiente estado.
     * Tiene efecto sobre el lote — solo llamar al persistir una etapa.
     */
    LoteState avanzar(LoteStateContext context);

    /**
     * Calcula el siguiente estado sin ejecutar la transición.
     * Usado únicamente para construir la respuesta (siguienteEtapa).
     * Los estados terminales (FINALIZADO, CANCELADO) retornan null.
     */
    default LoteState siguiente(LoteStateContext context) {
        return avanzar(context);
    }

    LoteState cancelar(LoteStateContext context);
}