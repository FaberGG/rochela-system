package com.rochela.rochelasystem.modulos.produccion.dto;

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
public class CierreLoteRequest {

    private Integer unidadesProducidas;
    private Double pesoTotalKg;
    private String observaciones;
}

