package com.rochela.rochelasystem.modulos.recepcion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(name = "ValidacionDetalle", description = "Detalle de validacion por parametro")
public class ValidacionDetalleDto {

    @Schema(description = "Valor obtenido", example = "3.2")
    private final Double valor;

    @Schema(description = "Rango minimo", example = "3.0")
    private final Double rangoMin;

    @Schema(description = "Rango maximo", example = "3.8")
    private final Double rangoMax;

    @Schema(description = "Indica si el valor es apto", example = "true")
    private final Boolean apto;
}
