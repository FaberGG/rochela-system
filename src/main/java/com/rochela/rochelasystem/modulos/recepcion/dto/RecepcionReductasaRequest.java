package com.rochela.rochelasystem.modulos.recepcion.dto;

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
public class RecepcionReductasaRequest {

    private LocalTime horaFinReductasa;
}
