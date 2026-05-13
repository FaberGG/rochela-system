package com.rochela.rochelasystem.modulos.produccion.dto;

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
public class PrensadoRequest {

    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Double presionPsi;
    private String responsable;
}

