package com.rochela.rochelasystem.modulos.exportacion.listener;

import com.rochela.rochelasystem.modulos.exportacion.service.ExportacionVistaService;
import com.rochela.rochelasystem.shared.event.RecepcionCompletadaEvent;
import com.rochela.rochelasystem.shared.event.RecepcionPendienteReductasaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class RecepcionCompletadaExportListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecepcionCompletadaExportListener.class);

    private final ExportacionVistaService exportacionVistaService;
    private final boolean recepcionPendienteEnabled;

    public RecepcionCompletadaExportListener(ExportacionVistaService exportacionVistaService,
                                             @Value("${exportacion.recepcion.pendiente.enabled:false}")
                                             boolean recepcionPendienteEnabled) {
        this.exportacionVistaService = exportacionVistaService;
        this.recepcionPendienteEnabled = recepcionPendienteEnabled;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onRecepcionCompletada(RecepcionCompletadaEvent event) {
        LOGGER.info("Procesando RecepcionCompletadaEvent recepcionId={}", event.getRecepcionId());
        exportacionVistaService.registrarRecepcion(event.getRecepcionId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onRecepcionPendiente(RecepcionPendienteReductasaEvent event) {
        if (!recepcionPendienteEnabled) {
            return;
        }
        LOGGER.info("Procesando RecepcionPendienteReductasaEvent recepcionId={}", event.getRecepcionId());
        exportacionVistaService.registrarRecepcion(event.getRecepcionId());
    }
}
