package com.rochela.rochelasystem.modulos.produccion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
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
@Schema(name = "CorteCreateRequest", description = "Datos para registrar un corte")
public class CorteCreateRequest {

    @Schema(description = "Hora del corte", example = "10:15:00")
    private LocalTime hora;

    @Schema(description = "Observacion", example = "Corte uniforme")
    private String observacion;
}
