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
@Schema(name = "SaladoRequest", description = "Datos para registrar salado")
public class SaladoRequest {

    @Schema(description = "Hora de registro", example = "11:00:00")
    private LocalTime hora;

    @Schema(description = "Temperatura en grados Celsius", example = "12.0")
    private Double temperatura;

    @Schema(description = "Cantidad en kg", example = "20.0")
    private Double cantidadKg;

    @Schema(description = "Sodio inicial", example = "1.2")
    private Double sodioInicial;

    @Schema(description = "Sodio final", example = "1.8")
    private Double sodioFinal;

    @Schema(description = "Lote de sal", example = "SAL-2026-05")
    private String loteSal;
}
