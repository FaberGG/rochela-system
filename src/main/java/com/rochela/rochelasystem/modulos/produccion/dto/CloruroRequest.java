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
@Schema(name = "CloruroRequest", description = "Datos para registrar cloruro")
public class CloruroRequest {

    @Schema(description = "Hora de registro", example = "09:00:00")
    private LocalTime hora;

    @Schema(description = "Temperatura en grados Celsius", example = "35.0")
    private Double temperatura;

    @Schema(description = "Cantidad en gramos", example = "12.0")
    private Double cantidadGramos;

    @Schema(description = "Lote del cloruro", example = "CL-2026-05")
    private String loteCloruro;
}

