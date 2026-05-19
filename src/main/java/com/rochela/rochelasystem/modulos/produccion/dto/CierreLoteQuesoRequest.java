package com.rochela.rochelasystem.modulos.produccion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "CierreLoteQuesoRequest", description = "Datos para cerrar un lote")
public class CierreLoteQuesoRequest {

    @Schema(description = "Unidades producidas", example = "250")
    private Integer unidadesProducidas;

    @Schema(description = "Peso total en kg", example = "120.5")
    private Double pesoTotalKg;

    @Schema(description = "Observaciones del cierre", example = "Proceso estable")
    private String observaciones;
}

