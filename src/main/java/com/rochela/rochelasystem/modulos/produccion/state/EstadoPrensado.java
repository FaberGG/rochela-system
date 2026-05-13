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

	@Override
	public LoteState avanzar(LoteStateContext context) {
		return resolver.resolve(EstadoLote.FINALIZADO);
	}

	@Override
	public LoteState cancelar(LoteStateContext context) {
		return resolver.resolve(EstadoLote.CANCELADO);
	}
}

