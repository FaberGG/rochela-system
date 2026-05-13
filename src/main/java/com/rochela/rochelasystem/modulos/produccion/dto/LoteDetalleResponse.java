package com.rochela.rochelasystem.modulos.produccion.dto;

import com.rochela.rochelasystem.shared.enums.EstadoLote;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoteDetalleResponse {

    private Long id;
    private String codigoLote;
    private ProductoResumenDto producto;
    private LocalDateTime fechaHoraInicio;
    private LocalDate fechaVencimiento;
    private EstadoLote estadoActual;
    private EstadoLote siguienteEtapa;
    private List<EtapaDetalleDto> etapas;
    private List<CorteDetalleDto> cortes;
    private CierreLoteResponse cierre;
}

