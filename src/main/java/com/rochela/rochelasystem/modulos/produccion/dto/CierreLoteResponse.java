package com.rochela.rochelasystem.modulos.produccion.dto;

import com.rochela.rochelasystem.shared.enums.EstadoLote;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CierreLoteResponse {

    private String codigoLote;
    private EstadoLote estadoActual;
    private LocalDateTime fechaHoraCierre;
    private Integer unidadesProducidas;
    private Double pesoTotalKg;
    private Double rendimientoTeorico;
    private Double rendimientoGeneral;
}

