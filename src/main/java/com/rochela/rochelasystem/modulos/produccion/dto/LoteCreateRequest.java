package com.rochela.rochelasystem.modulos.produccion.dto;

import java.time.LocalDateTime;
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
public class LoteCreateRequest {

    private String productoCodigo;
    private LocalDateTime fechaHoraInicio;
    private Long recepcionLecheId;
}

