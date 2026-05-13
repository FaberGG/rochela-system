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

    @DisplayName("El estado CORTES avanza a LAVADO_DESUERADO si hay lavado, o a DESUERADO si no lo hay")
    @Test
    void cortes_avanza_a_lavado_o_desuerado() {
        StateResolver resolver = new StateResolver();
        LoteStateContext conLavado = new LoteStateContext(false, false, true, resolver);
        LoteStateContext sinLavado = new LoteStateContext(false, false, false, resolver);

        LoteState cortes = resolver.resolve(EstadoLote.CORTES);

        assertEquals(EstadoLote.LAVADO_DESUERADO, cortes.avanzar(conLavado).getEstado());
        assertEquals(EstadoLote.DESUERADO, cortes.avanzar(sinLavado).getEstado());
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

