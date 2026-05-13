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
public class PasteurizacionRequest {

    private LocalTime hora;
    private Double temperatura;
}

