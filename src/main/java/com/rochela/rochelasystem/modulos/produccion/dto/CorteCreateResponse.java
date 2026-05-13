package com.rochela.rochelasystem.modulos.produccion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "CorteCreateResponse", description = "Resultado al registrar un corte")
public class CorteCreateResponse {

    @Schema(description = "Numero consecutivo del corte", example = "2")
    private Integer numeroCorte;

    @Schema(description = "Hora del corte", example = "10:15:00")
    private LocalTime hora;

    @Schema(description = "Observacion", example = "Corte uniforme")
    private String observacion;

    @Schema(description = "Total de cortes registrados", example = "2")
    private Long totalCortesRegistrados;
}

