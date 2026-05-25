package com.rochela.rochelasystem.modulos.exportacion.listener;

import com.rochela.rochelasystem.modulos.exportacion.service.ExportacionVistaService;
import com.rochela.rochelasystem.shared.event.LoteQuesoCerradoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class LoteQuesoCerradoExportListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoteQuesoCerradoExportListener.class);

    private final ExportacionVistaService exportacionVistaService;

    public LoteQuesoCerradoExportListener(ExportacionVistaService exportacionVistaService) {
        this.exportacionVistaService = exportacionVistaService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onLoteQuesoCerrado(LoteQuesoCerradoEvent event) {
        LOGGER.info("Procesando LoteQuesoCerradoEvent loteQuesoId={}", event.getLoteQuesoId());
        exportacionVistaService.registrarLoteQuesoCerrado(event.getLoteQuesoId());
    }
}
