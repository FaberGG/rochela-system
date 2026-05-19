package com.rochela.rochelasystem.modulos.produccion.dto;

import com.rochela.rochelasystem.shared.enums.EstadoLote;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "CierreLoteQuesoResponse", description = "Resultado del cierre del lote")
public class CierreLoteQuesoResponse {

    @Schema(description = "Codigo del lote", example = "L-132261-01")
    private String codigoLote;

    @Schema(description = "Estado actual del lote", example = "FINALIZADO")
    private EstadoLote estadoActual;

    @Schema(description = "Fecha y hora de cierre", example = "2026-05-13T13:00:00")
    private LocalDateTime fechaHoraCierre;

    @Schema(description = "Unidades producidas", example = "250")
    private Integer unidadesProducidas;

    @Schema(description = "Peso total en kg", example = "120.5")
    private Double pesoTotalKg;

    @Schema(description = "Rendimiento teorico", example = "85.0")
    private Double rendimientoTeorico;

    @Schema(description = "Rendimiento general", example = "82.5")
    private Double rendimientoGeneral;
}

