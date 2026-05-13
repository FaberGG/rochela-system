package com.rochela.rochelasystem.modulos.produccion.state;

import com.rochela.rochelasystem.shared.enums.EstadoLote;

public class EstadoLavadoDesuerado implements LoteState {

	private final StateResolver resolver;

	public EstadoLavadoDesuerado(StateResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	public EstadoLote getEstado() {
		return EstadoLote.LAVADO_DESUERADO;
	}

	@Override
	public LoteState avanzar(LoteStateContext context) {
		return resolver.resolve(EstadoLote.DESUERADO);
	}

	@Override
	public LoteState cancelar(LoteStateContext context) {
		return resolver.resolve(EstadoLote.CANCELADO);
	}
}

