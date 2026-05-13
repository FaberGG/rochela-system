package com.rochela.rochelasystem.modulos.produccion.state;

import com.rochela.rochelasystem.shared.enums.EstadoLote;

public class EstadoFinalizado implements LoteState {

	private final StateResolver resolver;

	public EstadoFinalizado(StateResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	public EstadoLote getEstado() {
		return EstadoLote.FINALIZADO;
	}

	@Override
	public LoteState avanzar(LoteStateContext context) {
		throw new IllegalStateException("No se puede avanzar desde FINALIZADO.");
	}

	@Override
	public LoteState cancelar(LoteStateContext context) {
		throw new IllegalStateException("No se puede cancelar un lote FINALIZADO.");
	}
}

