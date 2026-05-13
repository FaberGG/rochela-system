package com.rochela.rochelasystem.modulos.recepcion.dto;

import com.rochela.rochelasystem.shared.enums.EstadoRecepcion;
import com.rochela.rochelasystem.shared.enums.ResultadoValidacion;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(name = "RecepcionReductasaResponse", description = "Resultado del cierre de reductasa")
public class RecepcionReductasaResponse {

    @Schema(description = "Id de la recepcion", example = "1")
    private final Long id;

    @Schema(description = "Hora inicio reductasa", example = "09:00:00")
    private final LocalTime horaInicioReductasa;

    @Schema(description = "Hora fin reductasa", example = "11:30:00")
    private final LocalTime horaFinReductasa;

    @Schema(description = "Tiempo de reductasa en minutos", example = "150")
    private final Integer tiempoReductasaMinutos;

    @Schema(description = "Cumple reductasa", example = "true")
    private final Boolean cumpleReductasa;

    @Schema(description = "Estado de la recepcion", example = "COMPLETA")
    private final EstadoRecepcion estadoRecepcion;

    @Schema(description = "Resultado de validacion", example = "APTA")
    private final ResultadoValidacion resultadoValidacion;
}
