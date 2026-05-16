package com.rochela.rochelasystem.modulos.produccion.state;

import com.rochela.rochelasystem.shared.enums.EstadoLote;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoteStateTest {

    @DisplayName("El estado INICIADO avanza correctamente según los flags de pasteurización y cloruro")
    @Test
    void iniciado_avanza_segun_flags() {
        StateResolver resolver = new StateResolver();
        LoteStateContext conPasteurizacion = new LoteStateContext(true, true, false, resolver);
        LoteStateContext sinPasteurizacionConCloruro = new LoteStateContext(false, true, false, resolver);
        LoteStateContext sinPasteurizacionSinCloruro = new LoteStateContext(false, false, false, resolver);

        LoteState iniciado = resolver.resolve(EstadoLote.INICIADO);

        assertEquals(EstadoLote.PASTEURIZACION, iniciado.avanzar(conPasteurizacion).getEstado());
        assertEquals(EstadoLote.CLORURO, iniciado.avanzar(sinPasteurizacionConCloruro).getEstado());
        assertEquals(EstadoLote.CUAJO, iniciado.avanzar(sinPasteurizacionSinCloruro).getEstado());
    }

    @DisplayName("El estado CORTES avanza a CORTES_CERRADOS")
    @Test
    void cortes_avanza_a_cortes_cerrados() {
        StateResolver resolver = new StateResolver();
        LoteState cortes = resolver.resolve(EstadoLote.CORTES);

        assertEquals(EstadoLote.CORTES_CERRADOS, cortes.avanzar(new LoteStateContext(false, false, false, resolver)).getEstado());
    }

    @DisplayName("El estado CORTES_CERRADOS avanza a LAVADO_DESUERADO o DESUERADO")
    @Test
    void cortes_cerrados_avanza_segun_lavado() {
        StateResolver resolver = new StateResolver();
        LoteStateContext conLavado = new LoteStateContext(false, false, true, resolver);
        LoteStateContext sinLavado = new LoteStateContext(false, false, false, resolver);

        LoteState cortesCerrados = resolver.resolve(EstadoLote.CORTES_CERRADOS);

        assertEquals(EstadoLote.LAVADO_DESUERADO, cortesCerrados.avanzar(conLavado).getEstado());
        assertEquals(EstadoLote.DESUERADO, cortesCerrados.avanzar(sinLavado).getEstado());
    }

    @DisplayName("Los estados terminales FINALIZADO y CANCELADO no permiten avanzar")
    @Test
    void estados_terminales_no_avanzan() {
        StateResolver resolver = new StateResolver();
        LoteStateContext context = new LoteStateContext(false, false, false, resolver);

        LoteState finalizado = resolver.resolve(EstadoLote.FINALIZADO);
        LoteState cancelado = resolver.resolve(EstadoLote.CANCELADO);

        assertThrows(IllegalStateException.class, () -> finalizado.avanzar(context));
        assertThrows(IllegalStateException.class, () -> cancelado.avanzar(context));
    }
    
}
