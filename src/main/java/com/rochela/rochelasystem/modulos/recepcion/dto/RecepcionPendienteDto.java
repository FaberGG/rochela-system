package com.rochela.rochelasystem.modulos.recepcion.dto;

import com.rochela.rochelasystem.shared.enums.Jornada;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(name = "RecepcionPendiente", description = "Recepcion con reductasa pendiente")
public class RecepcionPendienteDto {

    @Schema(description = "Id de la recepcion", example = "1")
    private final Long id;

    @Schema(description = "Fecha de recepcion", example = "2026-05-13")
    private final LocalDate fecha;

    @Schema(description = "Jornada", example = "AM")
    private final Jornada jornada;

    @Schema(description = "Nombre del proveedor", example = "Lacteos del Norte")
    private final String proveedor;

    @Schema(description = "Hora inicio reductasa", example = "09:00:00")
    private final LocalTime horaInicioReductasa;

    @Schema(description = "Minutos transcurridos", example = "120")
    private final long minutosTranscurridos;

    @Schema(description = "Fecha y hora de recepcion", example = "2026-05-13T09:05:00")
    private final LocalDateTime fechaHora;
}
