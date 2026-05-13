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
@Schema(name = "CuajoRequest", description = "Datos para registrar cuajo")
public class CuajoRequest {

    @Schema(description = "Hora de registro", example = "09:15:00")
    private LocalTime hora;

    @Schema(description = "Temperatura en grados Celsius", example = "32.0")
    private Double temperatura;

    @Schema(description = "Cantidad en gramos", example = "8.0")
    private Double cantidadGramos;

    @Schema(description = "Lote del cuajo", example = "CU-2026-05")
    private String loteCuajo;
}

