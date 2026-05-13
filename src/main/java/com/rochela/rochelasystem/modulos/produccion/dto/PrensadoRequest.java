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
@Schema(name = "PrensadoRequest", description = "Datos para registrar prensado")
public class PrensadoRequest {

    @Schema(description = "Hora de inicio", example = "11:30:00")
    private LocalTime horaInicio;

    @Schema(description = "Hora de fin", example = "12:00:00")
    private LocalTime horaFin;

    @Schema(description = "Presion en psi", example = "20.0")
    private Double presionPsi;

    @Schema(description = "Responsable", example = "Operario 1")
    private String responsable;
}

