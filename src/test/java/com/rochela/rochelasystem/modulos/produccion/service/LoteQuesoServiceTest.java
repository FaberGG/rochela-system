package com.rochela.rochelasystem.modulos.produccion.service;

import com.rochela.rochelasystem.modulos.catalogo.model.Producto;
import com.rochela.rochelasystem.modulos.catalogo.repository.ProductoRepository;
import com.rochela.rochelasystem.modulos.produccion.dto.CorteCreateRequest;
import com.rochela.rochelasystem.modulos.produccion.model.Corte;
import com.rochela.rochelasystem.modulos.produccion.model.LoteQueso;
import com.rochela.rochelasystem.modulos.produccion.repository.CierreLoteQuesoRepository;
import com.rochela.rochelasystem.modulos.produccion.repository.CorteRepository;
import com.rochela.rochelasystem.modulos.produccion.repository.EtapaPrensadoRepository;
import com.rochela.rochelasystem.modulos.produccion.repository.EtapaRegistroRepository;
import com.rochela.rochelasystem.modulos.produccion.repository.LoteLecheRepository;
import com.rochela.rochelasystem.modulos.produccion.repository.LoteQuesoRepository;
import com.rochela.rochelasystem.shared.enums.EstadoLote;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoteQuesoServiceTest {

    @Mock
    private LoteQuesoRepository loteQuesoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private LoteLecheRepository loteLecheRepository;

    @Mock
    private EtapaRegistroRepository etapaRegistroRepository;

    @Mock
    private EtapaPrensadoRepository etapaPrensadoRepository;

    @Mock
    private CorteRepository corteRepository;

    @Mock
    private CierreLoteQuesoRepository cierreLoteQuesoRepository;

    @InjectMocks
    private LoteService loteService;

    @DisplayName("El primer corte cambia el lote de CUAJO a CORTES")
    @Test
    void agregarCorte_avanza_de_cuajo_a_cortes() {
        LoteQueso loteQueso = new LoteQueso();
        loteQueso.setId(1L);
        loteQueso.setCodigoLote("L001");
        loteQueso.setProductoId(10L);
        loteQueso.setFechaHoraInicio(LocalDateTime.of(2026, 5, 15, 8, 0));
        loteQueso.setFechaVencimiento(LocalDate.of(2026, 6, 14));
        loteQueso.setEstadoActual(EstadoLote.CUAJO);
        loteQueso.setBatchDelDia(1);

        Producto producto = Producto.builder()
                .id(10L)
                .codigo("QUESO-001")
                .nombre("Queso")
                .requierePasteurizacion(false)
                .requiereCloruro(false)
                .requiereLavadoDesuerado(false)
                .build();

        when(loteQuesoRepository.findById(1L)).thenReturn(Optional.of(loteQueso));
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));
        when(corteRepository.findTopByLoteIdOrderByNumeroCorteDesc(1L)).thenReturn(Optional.empty());
        when(corteRepository.save(any(Corte.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(corteRepository.countByLoteId(1L)).thenReturn(1L);
        when(loteQuesoRepository.save(any(LoteQueso.class))).thenAnswer(invocation -> invocation.getArgument(0));

        loteService.agregarCorte(1L, CorteCreateRequest.builder()
                .hora(LocalTime.of(10, 15))
                .observacion("Primer corte")
                .build());

        ArgumentCaptor<LoteQueso> loteCaptor = ArgumentCaptor.forClass(LoteQueso.class);
        verify(loteQuesoRepository).save(loteCaptor.capture());
        assertEquals(EstadoLote.CORTES, loteCaptor.getValue().getEstadoActual());
    }

    @DisplayName("Iniciar prensado avanza el lote a PRENSADO_INICIADO y bloquea un segundo inicio")
    @Test
    void registrarPrensado_avanza_a_prensado_iniciado_y_bloquea_repeticion() {
        LoteQueso loteQueso = new LoteQueso();
        loteQueso.setId(2L);
        loteQueso.setCodigoLote("L002");
        loteQueso.setProductoId(20L);
        loteQueso.setFechaHoraInicio(LocalDateTime.of(2026, 5, 15, 8, 0));
        loteQueso.setFechaVencimiento(LocalDate.of(2026, 6, 14));
        loteQueso.setEstadoActual(EstadoLote.SALADO);
        loteQueso.setBatchDelDia(1);

        Producto producto = Producto.builder()
                .id(20L)
                .codigo("QUESO-002")
                .nombre("Queso maduro")
                .requierePasteurizacion(false)
                .requiereCloruro(false)
                .requiereLavadoDesuerado(false)
                .build();

        when(loteQuesoRepository.findById(2L)).thenReturn(Optional.of(loteQueso));
        when(productoRepository.findById(20L)).thenReturn(Optional.of(producto));
        when(etapaRegistroRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(loteQuesoRepository.save(any(LoteQueso.class))).thenAnswer(invocation -> invocation.getArgument(0));

        loteService.registrarPrensado(2L, com.rochela.rochelasystem.modulos.produccion.dto.PrensadoRequest.builder()
                .horaInicio(LocalTime.of(15, 0))
                .presionPsi(12.5)
                .build());

        assertEquals(EstadoLote.PRENSADO_INICIADO, loteQueso.getEstadoActual());
        assertThrows(com.rochela.rochelasystem.shared.exception.EstadoInvalidoException.class, () ->
                loteService.registrarPrensado(2L, com.rochela.rochelasystem.modulos.produccion.dto.PrensadoRequest.builder()
                        .horaInicio(LocalTime.of(16, 0))
                        .presionPsi(13.0)
                        .build()));
    }
}
