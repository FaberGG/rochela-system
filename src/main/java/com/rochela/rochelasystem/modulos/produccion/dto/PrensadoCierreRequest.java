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
@Schema(name = "PrensadoCierreRequest", description = "Datos para cerrar el prensado")
public class PrensadoCierreRequest {

    @Schema(description = "Hora de fin", example = "12:00:00")
    private LocalTime horaFin;

    @Schema(description = "Responsable", example = "Operario 1")
    private String responsable;
}

