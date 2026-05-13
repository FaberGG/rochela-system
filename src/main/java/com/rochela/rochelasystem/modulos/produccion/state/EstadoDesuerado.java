package com.rochela.rochelasystem.modulos.produccion.state;

import com.rochela.rochelasystem.shared.enums.EstadoLote;

public class EstadoDesuerado implements LoteState {

	private final StateResolver resolver;

	public EstadoDesuerado(StateResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	public EstadoLote getEstado() {
		return EstadoLote.DESUERADO;
	}

	@Override
	public LoteState avanzar(LoteStateContext context) {
		return resolver.resolve(EstadoLote.SALADO);
	}

	@Override
	public LoteState cancelar(LoteStateContext context) {
		return resolver.resolve(EstadoLote.CANCELADO);
	}
}

