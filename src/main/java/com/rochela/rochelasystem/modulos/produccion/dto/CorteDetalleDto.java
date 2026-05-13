package com.rochela.rochelasystem.modulos.produccion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "CorteDetalle", description = "Detalle de un corte registrado")
public class CorteDetalleDto {

    @Schema(description = "Numero consecutivo del corte", example = "1")
    private Integer numeroCorte;

    @Schema(description = "Hora del corte", example = "10:15:00")
    private LocalTime hora;

    @Schema(description = "Observacion", example = "Corte uniforme")
    private String observacion;
}

