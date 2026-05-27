package com.rochela.rochelasystem.modulos.operario.service;

import com.rochela.rochelasystem.modulos.operario.dto.OperarioFeedResponse;
import com.rochela.rochelasystem.modulos.produccion.dto.LoteResumenResponse;
import com.rochela.rochelasystem.modulos.produccion.service.LoteService;
import com.rochela.rochelasystem.modulos.recepcion.dto.RecepcionPendienteDto;
import com.rochela.rochelasystem.modulos.recepcion.service.RecepcionService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OperarioFeedService {

    private final RecepcionService recepcionService;
    private final LoteService loteService;

    public OperarioFeedService(RecepcionService recepcionService, LoteService loteService) {
        this.recepcionService = recepcionService;
        this.loteService = loteService;
    }

    @Transactional(readOnly = true)
    public OperarioFeedResponse obtenerFeed() {
        System.out.println("ZoneId: " + ZoneId.systemDefault());
        System.out.println("Now: " + LocalDateTime.now());
        System.out.println("Zoned: " + ZonedDateTime.now());
        List<RecepcionPendienteDto> pendientes = recepcionService.listarPendientes();
        List<LoteResumenResponse> lotesActivos = loteService.listarLotes(null, null, null, null, true, null);
        return OperarioFeedResponse.builder()
                .pendientes(pendientes)
                .lotesActivos(lotesActivos)
                .build();
    }
}
