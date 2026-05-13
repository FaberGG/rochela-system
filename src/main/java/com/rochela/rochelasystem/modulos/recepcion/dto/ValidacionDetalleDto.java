package com.rochela.rochelasystem.modulos.recepcion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ValidacionDetalleDto {

    private final Double valor;
    private final Double rangoMin;
    private final Double rangoMax;
    private final Boolean apto;
}
