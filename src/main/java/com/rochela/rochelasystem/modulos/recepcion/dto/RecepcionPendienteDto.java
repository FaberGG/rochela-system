package com.rochela.rochelasystem.modulos.recepcion.dto;

import com.rochela.rochelasystem.shared.enums.Jornada;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RecepcionPendienteDto {

    private final Long id;
    private final LocalDate fecha;
    private final Jornada jornada;
    private final String proveedor;
    private final LocalTime horaInicioReductasa;
    private final long minutosTranscurridos;
}
