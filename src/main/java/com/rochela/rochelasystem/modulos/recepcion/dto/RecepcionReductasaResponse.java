package com.rochela.rochelasystem.modulos.recepcion.dto;

import com.rochela.rochelasystem.shared.enums.EstadoRecepcion;
import com.rochela.rochelasystem.shared.enums.ResultadoValidacion;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RecepcionReductasaResponse {

    private final Long id;
    private final LocalTime horaInicioReductasa;
    private final LocalTime horaFinReductasa;
    private final Integer tiempoReductasaMinutos;
    private final Boolean cumpleReductasa;
    private final EstadoRecepcion estadoRecepcion;
    private final ResultadoValidacion resultadoValidacion;
}
