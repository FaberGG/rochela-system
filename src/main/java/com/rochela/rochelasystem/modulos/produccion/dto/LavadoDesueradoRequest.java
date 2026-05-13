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
@Schema(name = "LavadoDesueradoRequest", description = "Datos para registrar lavado y desuerado")
public class LavadoDesueradoRequest {

    @Schema(description = "Hora de registro", example = "10:00:00")
    private LocalTime hora;

    @Schema(description = "Litros", example = "150.0")
    private Double litros;
}

