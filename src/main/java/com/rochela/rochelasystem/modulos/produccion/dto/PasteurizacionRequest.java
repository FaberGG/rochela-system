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
@Schema(name = "PasteurizacionRequest", description = "Datos para registrar pasteurizacion")
public class PasteurizacionRequest {

    @Schema(description = "Hora de registro", example = "08:45:00")
    private LocalTime hora;

    @Schema(description = "Temperatura en grados Celsius", example = "72.5")
    private Double temperatura;
}

