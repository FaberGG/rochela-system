package com.rochela.rochelasystem.modulos.produccion.dto;

import com.rochela.rochelasystem.shared.enums.EstadoLote;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "LoteResumenResponse", description = "Resumen del lote")
public class LoteResumenResponse {

    @Schema(description = "Id del lote", example = "1")
    private Long id;

    @Schema(description = "Codigo del lote", example = "L-20260513-01")
    private String codigoLote;

    @Schema(description = "Producto asociado")
    private ProductoResumenDto producto;

    @Schema(description = "Fecha y hora de inicio", example = "2026-05-13T08:30:00")
    private LocalDateTime fechaHoraInicio;

    @Schema(description = "Fecha de vencimiento", example = "2026-06-13")
    private LocalDate fechaVencimiento;

    @Schema(description = "Estado actual del lote", example = "INICIADO")
    private EstadoLote estadoActual;

    @Schema(description = "Siguiente etapa esperada", example = "PASTEURIZACION")
    private EstadoLote siguienteEtapa;
}

