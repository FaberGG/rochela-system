package com.rochela.rochelasystem.modulos.produccion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LoteCreateRequest", description = "Datos para crear un lote de produccion")
public class LoteCreateRequest {

    @Schema(description = "Codigo del producto", example = "QUESO-001")
    private String productoCodigo;

    @Schema(description = "Fecha y hora de inicio", example = "2026-05-13T08:30:00")
    private LocalDateTime fechaHoraInicio;

    @Schema(description = "Id del lote de leche", example = "5")
    private Long loteLecheId;

    @Schema(description = "Litros de leche usados en el lote", example = "500.0")
    private Double cantidadLitrosUsados;
}
