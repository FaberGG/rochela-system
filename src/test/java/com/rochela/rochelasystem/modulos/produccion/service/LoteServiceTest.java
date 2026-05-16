package com.rochela.rochelasystem.modulos.produccion.service;

import com.rochela.rochelasystem.modulos.catalogo.model.Producto;
import com.rochela.rochelasystem.modulos.catalogo.repository.ProductoRepository;
import com.rochela.rochelasystem.modulos.produccion.dto.CorteCreateRequest;
import com.rochela.rochelasystem.modulos.produccion.model.Corte;
import com.rochela.rochelasystem.modulos.produccion.model.Lote;
import com.rochela.rochelasystem.modulos.produccion.repository.CierreLoteRepository;
import com.rochela.rochelasystem.modulos.produccion.repository.CorteRepository;
import com.rochela.rochelasystem.modulos.produccion.repository.EtapaRegistroRepository;
import com.rochela.rochelasystem.modulos.produccion.repository.LoteRepository;
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
class LoteServiceTest {

    @Mock
    private LoteRepository loteRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private com.rochela.rochelasystem.modulos.recepcion.repository.RecepcionLecheRepository recepcionLecheRepository;

    @Mock
    private EtapaRegistroRepository etapaRegistroRepository;

    @Mock
    private CorteRepository corteRepository;

    @Mock
    private CierreLoteRepository cierreLoteRepository;

    @InjectMocks
    private LoteService loteService;

    @DisplayName("El primer corte cambia el lote de CUAJO a CORTES")
    @Test
    void agregarCorte_avanza_de_cuajo_a_cortes() {
        Lote lote = new Lote();
        lote.setId(1L);
        lote.setCodigoLote("L001");
        lote.setProductoId(10L);
        lote.setFechaHoraInicio(LocalDateTime.of(2026, 5, 15, 8, 0));
        lote.setFechaVencimiento(LocalDate.of(2026, 6, 14));
        lote.setEstadoActual(EstadoLote.CUAJO);
        lote.setBatchDelDia(1);

        Producto producto = Producto.builder()
                .id(10L)
                .codigo("QUESO-001")
                .nombre("Queso")
                .requierePasteurizacion(false)
                .requiereCloruro(false)
                .requiereLavadoDesuerado(false)
                .build();

        when(loteRepository.findById(1L)).thenReturn(Optional.of(lote));
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));
        when(corteRepository.findTopByLoteIdOrderByNumeroCorteDesc(1L)).thenReturn(Optional.empty());
        when(corteRepository.save(any(Corte.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(corteRepository.countByLoteId(1L)).thenReturn(1L);
        when(loteRepository.save(any(Lote.class))).thenAnswer(invocation -> invocation.getArgument(0));

        loteService.agregarCorte(1L, CorteCreateRequest.builder()
                .hora(LocalTime.of(10, 15))
                .observacion("Primer corte")
                .build());

        ArgumentCaptor<Lote> loteCaptor = ArgumentCaptor.forClass(Lote.class);
        verify(loteRepository).save(loteCaptor.capture());
        assertEquals(EstadoLote.CORTES, loteCaptor.getValue().getEstadoActual());
    }

    @DisplayName("Registrar prensado avanza el lote a PRENSADO y bloquea un segundo registro")
    @Test
    void registrarPrensado_avanza_a_prensado_y_bloquea_repeticion() {
        Lote lote = new Lote();
        lote.setId(2L);
        lote.setCodigoLote("L002");
        lote.setProductoId(20L);
        lote.setFechaHoraInicio(LocalDateTime.of(2026, 5, 15, 8, 0));
        lote.setFechaVencimiento(LocalDate.of(2026, 6, 14));
        lote.setEstadoActual(EstadoLote.SALADO);
        lote.setBatchDelDia(1);

        Producto producto = Producto.builder()
                .id(20L)
                .codigo("QUESO-002")
                .nombre("Queso maduro")
                .requierePasteurizacion(false)
                .requiereCloruro(false)
                .requiereLavadoDesuerado(false)
                .build();

        when(loteRepository.findById(2L)).thenReturn(Optional.of(lote));
        when(productoRepository.findById(20L)).thenReturn(Optional.of(producto));
        when(etapaRegistroRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(loteRepository.save(any(Lote.class))).thenAnswer(invocation -> invocation.getArgument(0));

        loteService.registrarPrensado(2L, com.rochela.rochelasystem.modulos.produccion.dto.PrensadoRequest.builder()
                .horaInicio(LocalTime.of(15, 0))
                .horaFin(LocalTime.of(16, 0))
                .presionPsi(12.5)
                .responsable("Operario")
                .build());

        assertEquals(EstadoLote.PRENSADO, lote.getEstadoActual());
        assertThrows(com.rochela.rochelasystem.shared.exception.EstadoInvalidoException.class, () ->
                loteService.registrarPrensado(2L, com.rochela.rochelasystem.modulos.produccion.dto.PrensadoRequest.builder()
                        .horaInicio(LocalTime.of(16, 0))
                        .horaFin(LocalTime.of(17, 0))
                        .presionPsi(13.0)
                        .responsable("Operario")
                        .build()));
    }
}
