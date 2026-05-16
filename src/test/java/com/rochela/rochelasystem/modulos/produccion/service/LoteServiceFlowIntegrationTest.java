package com.rochela.rochelasystem.modulos.produccion.service;

import com.rochela.rochelasystem.modulos.catalogo.model.Producto;
import com.rochela.rochelasystem.modulos.catalogo.repository.ProductoRepository;
import com.rochela.rochelasystem.modulos.produccion.dto.CierreLoteRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.CloruroRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.CorteCreateRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.CuajoRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.DesueradoRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.LavadoDesueradoRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.LoteCreateRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.LoteResumenResponse;
import com.rochela.rochelasystem.modulos.produccion.dto.PasteurizacionRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.PrensadoRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.SaladoRequest;
import com.rochela.rochelasystem.modulos.produccion.repository.CorteRepository;
import com.rochela.rochelasystem.modulos.produccion.repository.LoteRepository;
import com.rochela.rochelasystem.shared.enums.EstadoLote;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class LoteServiceFlowIntegrationTest {

    @Autowired
    private LoteService loteService;

    @Autowired
    private LoteRepository loteRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CorteRepository corteRepository;

    @DisplayName("Flujo completo para producto sin pasteurizacion ni cloruro")
    @Test
    void flujoCompleto_sin_pasteurizacion_ni_cloruro() {
        LoteResumenResponse lote = crearLote("QF01");
        assertEquals(EstadoLote.INICIADO, lote.getEstadoActual());

        lote = loteService.registrarCuajo(lote.getId(), cuajo());
        assertEstado(lote.getId(), EstadoLote.CUAJO);
        assertEquals(EstadoLote.CORTES, lote.getSiguienteEtapa());

        loteService.agregarCorte(lote.getId(), corte("Primer corte"));
        assertEstado(lote.getId(), EstadoLote.CORTES);

        loteService.agregarCorte(lote.getId(), corte("Segundo corte"));
        assertEstado(lote.getId(), EstadoLote.CORTES);
        assertEquals(2L, corteRepository.countByLoteId(lote.getId()));

        lote = loteService.cerrarCortes(lote.getId());
        assertEquals(EstadoLote.CORTES_CERRADOS, lote.getEstadoActual());
        assertEquals(EstadoLote.DESUERADO, lote.getSiguienteEtapa());

        lote = loteService.registrarDesuerado(lote.getId(), desuerado());
        assertEquals(EstadoLote.DESUERADO, lote.getEstadoActual());
        assertEquals(EstadoLote.SALADO, lote.getSiguienteEtapa());

        lote = loteService.registrarSalado(lote.getId(), salado());
        assertEquals(EstadoLote.SALADO, lote.getEstadoActual());
        assertEquals(EstadoLote.PRENSADO, lote.getSiguienteEtapa());

        lote = loteService.registrarPrensado(lote.getId(), prensado());
        assertEquals(EstadoLote.PRENSADO, lote.getEstadoActual());
        assertEquals(EstadoLote.FINALIZADO, lote.getSiguienteEtapa());

        assertEquals(EstadoLote.FINALIZADO, loteService.cerrarLote(lote.getId(), cierre()).getEstadoActual());
        assertEstado(lote.getId(), EstadoLote.FINALIZADO);
    }

    @DisplayName("Flujo completo para producto con pasteurizacion y cloruro")
    @Test
    void flujoCompleto_con_pasteurizacion_y_cloruro() {
        LoteResumenResponse lote = crearLote("SP004");
        assertEquals(EstadoLote.INICIADO, lote.getEstadoActual());

        lote = loteService.registrarPasteurizacion(lote.getId(), pasteurizacion());
        assertEquals(EstadoLote.PASTEURIZACION, lote.getEstadoActual());

        lote = loteService.registrarCloruro(lote.getId(), cloruro());
        assertEquals(EstadoLote.CLORURO, lote.getEstadoActual());

        lote = loteService.registrarCuajo(lote.getId(), cuajo());
        assertEquals(EstadoLote.CUAJO, lote.getEstadoActual());
        assertEquals(EstadoLote.CORTES, lote.getSiguienteEtapa());

        loteService.agregarCorte(lote.getId(), corte("Primer corte"));
        loteService.agregarCorte(lote.getId(), corte("Segundo corte"));
        assertEstado(lote.getId(), EstadoLote.CORTES);
        assertEquals(2L, corteRepository.countByLoteId(lote.getId()));

        lote = loteService.cerrarCortes(lote.getId());
        assertEquals(EstadoLote.CORTES_CERRADOS, lote.getEstadoActual());

        lote = loteService.registrarDesuerado(lote.getId(), desuerado());
        assertEquals(EstadoLote.DESUERADO, lote.getEstadoActual());

        lote = loteService.registrarSalado(lote.getId(), salado());
        assertEquals(EstadoLote.SALADO, lote.getEstadoActual());

        lote = loteService.registrarPrensado(lote.getId(), prensado());
        assertEquals(EstadoLote.PRENSADO, lote.getEstadoActual());
        assertEquals(EstadoLote.FINALIZADO, lote.getSiguienteEtapa());

        assertEquals(EstadoLote.FINALIZADO, loteService.cerrarLote(lote.getId(), cierre()).getEstadoActual());
        assertEstado(lote.getId(), EstadoLote.FINALIZADO);
    }

    @DisplayName("Flujo completo con lavado/desuerado")
    @Test
    void flujoCompleto_con_lavado_desuerado() {
        Producto producto = productoRepository.save(Producto.builder()
                .codigo("PRUEBA-LAVADO")
                .nombre("Producto con lavado")
                .requierePasteurizacion(true)
                .requiereCloruro(true)
                .requiereLavadoDesuerado(true)
                .build());

        LoteResumenResponse lote = crearLote(producto.getCodigo());
        assertEquals(EstadoLote.INICIADO, lote.getEstadoActual());

        lote = loteService.registrarPasteurizacion(lote.getId(), pasteurizacion());
        assertEquals(EstadoLote.PASTEURIZACION, lote.getEstadoActual());

        lote = loteService.registrarCloruro(lote.getId(), cloruro());
        assertEquals(EstadoLote.CLORURO, lote.getEstadoActual());

        lote = loteService.registrarCuajo(lote.getId(), cuajo());
        assertEquals(EstadoLote.CUAJO, lote.getEstadoActual());

        loteService.agregarCorte(lote.getId(), corte("Primer corte"));
        loteService.agregarCorte(lote.getId(), corte("Segundo corte"));
        assertEstado(lote.getId(), EstadoLote.CORTES);
        assertEquals(2L, corteRepository.countByLoteId(lote.getId()));

        lote = loteService.cerrarCortes(lote.getId());
        assertEquals(EstadoLote.CORTES_CERRADOS, lote.getEstadoActual());
        assertEquals(EstadoLote.LAVADO_DESUERADO, lote.getSiguienteEtapa());

        lote = loteService.registrarLavadoDesuerado(lote.getId(), lavadoDesuerado());
        assertEquals(EstadoLote.LAVADO_DESUERADO, lote.getEstadoActual());
        assertEquals(EstadoLote.DESUERADO, lote.getSiguienteEtapa());

        lote = loteService.registrarDesuerado(lote.getId(), desuerado());
        assertEquals(EstadoLote.DESUERADO, lote.getEstadoActual());
        assertEquals(EstadoLote.SALADO, lote.getSiguienteEtapa());

        lote = loteService.registrarSalado(lote.getId(), salado());
        assertEquals(EstadoLote.SALADO, lote.getEstadoActual());
        assertEquals(EstadoLote.PRENSADO, lote.getSiguienteEtapa());

        lote = loteService.registrarPrensado(lote.getId(), prensado());
        assertEquals(EstadoLote.PRENSADO, lote.getEstadoActual());
        assertEquals(EstadoLote.FINALIZADO, lote.getSiguienteEtapa());

        assertEquals(EstadoLote.FINALIZADO, loteService.cerrarLote(lote.getId(), cierre()).getEstadoActual());
        assertEstado(lote.getId(), EstadoLote.FINALIZADO);
    }

    private LoteResumenResponse crearLote(String productoCodigo) {
        return loteService.crearLote(LoteCreateRequest.builder()
                .productoCodigo(productoCodigo)
                .fechaHoraInicio(LocalDateTime.of(2026, 5, 15, 8, 0))
                .build());
    }

    private void assertEstado(Long loteId, EstadoLote estado) {
        assertEquals(estado, loteRepository.findById(loteId).orElseThrow().getEstadoActual());
    }

    private PasteurizacionRequest pasteurizacion() {
        return PasteurizacionRequest.builder()
                .hora(LocalTime.of(8, 30))
                .temperatura(72.5)
                .build();
    }

    private CloruroRequest cloruro() {
        return CloruroRequest.builder()
                .hora(LocalTime.of(8, 45))
                .temperatura(35.0)
                .cantidadGramos(12.0)
                .loteCloruro("CL-2026-05")
                .build();
    }

    private CuajoRequest cuajo() {
        return CuajoRequest.builder()
                .hora(LocalTime.of(9, 0))
                .temperatura(32.0)
                .cantidadGramos(8.0)
                .loteCuajo("CU-2026-05")
                .build();
    }

    private CorteCreateRequest corte(String observacion) {
        return CorteCreateRequest.builder()
                .hora(LocalTime.of(9, 15))
                .observacion(observacion)
                .build();
    }

    private LavadoDesueradoRequest lavadoDesuerado() {
        return LavadoDesueradoRequest.builder()
                .hora(LocalTime.of(9, 30))
                .litros(150.0)
                .build();
    }

    private DesueradoRequest desuerado() {
        return DesueradoRequest.builder()
                .hora(LocalTime.of(9, 45))
                .litros(120.0)
                .build();
    }

    private SaladoRequest salado() {
        return SaladoRequest.builder()
                .hora(LocalTime.of(10, 0))
                .temperatura(12.0)
                .cantidadKg(20.0)
                .sodioInicial(1.2)
                .sodioFinal(1.8)
                .loteSal("SAL-2026-05")
                .build();
    }

    private PrensadoRequest prensado() {
        return PrensadoRequest.builder()
                .horaInicio(LocalTime.of(10, 15))
                .horaFin(LocalTime.of(10, 45))
                .presionPsi(20.0)
                .responsable("Operario 1")
                .build();
    }

    private CierreLoteRequest cierre() {
        return CierreLoteRequest.builder()
                .unidadesProducidas(250)
                .pesoTotalKg(120.5)
                .observaciones("Proceso estable")
                .build();
    }
}
