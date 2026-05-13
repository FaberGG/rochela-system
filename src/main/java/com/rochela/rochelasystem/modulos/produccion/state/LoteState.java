package com.rochela.rochelasystem.modulos.produccion.state;

import com.rochela.rochelasystem.shared.enums.EstadoLote;

public interface LoteState {

    EstadoLote getEstado();

    LoteState avanzar(LoteStateContext context);

    LoteState cancelar(LoteStateContext context);
}

