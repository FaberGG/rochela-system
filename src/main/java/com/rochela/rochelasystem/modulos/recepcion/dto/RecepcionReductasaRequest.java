package com.rochela.rochelasystem.modulos.recepcion.dto;

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
@Schema(name = "RecepcionReductasaRequest", description = "Datos para cerrar la prueba de reductasa")
public class RecepcionReductasaRequest {

    @Schema(description = "Hora fin reductasa", example = "11:30:00")
    private LocalTime horaFinReductasa;
}
