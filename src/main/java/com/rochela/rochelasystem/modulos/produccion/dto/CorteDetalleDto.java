package com.rochela.rochelasystem.modulos.produccion.dto;

import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CorteDetalleDto {

    private Integer numeroCorte;
    private LocalTime hora;
    private String observacion;
}

