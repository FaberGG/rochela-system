package com.rochela.rochelasystem.modulos.operario.dto;

import com.rochela.rochelasystem.modulos.produccion.dto.LoteResumenResponse;
import com.rochela.rochelasystem.modulos.recepcion.dto.RecepcionPendienteDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "OperarioFeedResponse", description = "Feed de pendientes y lotes activos para operario")
public class OperarioFeedResponse {

    @Schema(description = "Recepciones con reductasa pendiente")
    private List<RecepcionPendienteDto> pendientes;

    @Schema(description = "Lotes activos en produccion")
    private List<LoteResumenResponse> lotesActivos;
}

