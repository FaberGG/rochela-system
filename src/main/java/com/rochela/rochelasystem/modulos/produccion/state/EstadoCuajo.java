package com.rochela.rochelasystem.modulos.produccion.state;

import com.rochela.rochelasystem.shared.enums.EstadoLote;

public class EstadoCuajo implements LoteState {

	private final StateResolver resolver;

	public EstadoCuajo(StateResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	public EstadoLote getEstado() {
		return EstadoLote.CUAJO;
	}

	@Override
	public LoteState avanzar(LoteStateContext context) {
		return resolver.resolve(EstadoLote.CORTES);
	}

	@Override
	public LoteState cancelar(LoteStateContext context) {
		return resolver.resolve(EstadoLote.CANCELADO);
	}
}

