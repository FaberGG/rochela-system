package com.rochela.rochelasystem.modulos.recepcion.dto;

import com.rochela.rochelasystem.shared.enums.EstadoRecepcion;
import com.rochela.rochelasystem.shared.enums.ResultadoValidacion;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(name = "RecepcionResponse", description = "Detalle de validaciones de la recepcion")
public class RecepcionResponse {

    @Schema(description = "Id de la recepcion", example = "1")
    private final Long id;

    @Schema(description = "Estado de la recepcion", example = "COMPLETA")
    private final EstadoRecepcion estadoRecepcion;

    @Schema(description = "Resultado de validacion", example = "APTA")
    private final ResultadoValidacion resultadoValidacion;

    @Schema(description = "Detalle de validaciones por parametro")
    private final Map<String, ValidacionDetalleDto> validacionDetalle;
}
