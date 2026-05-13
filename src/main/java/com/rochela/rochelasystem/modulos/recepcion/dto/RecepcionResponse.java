package com.rochela.rochelasystem.modulos.recepcion.dto;

import com.rochela.rochelasystem.shared.enums.EstadoRecepcion;
import com.rochela.rochelasystem.shared.enums.ResultadoValidacion;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RecepcionResponse {

    private final Long id;
    private final EstadoRecepcion estadoRecepcion;
    private final ResultadoValidacion resultadoValidacion;
    private final Map<String, ValidacionDetalleDto> validacionDetalle;
}
