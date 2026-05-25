package com.rochela.rochelasystem.modulos.exportacion.controller;

import com.rochela.rochelasystem.modulos.exportacion.service.GoogleSheetsSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/exportacion")
public class ExportacionController {

    private final GoogleSheetsSyncService syncService;

    public ExportacionController(GoogleSheetsSyncService syncService) {
        this.syncService = syncService;
    }

    // Endpoint para forzar la sincronización en el acto
    @PostMapping("/sincronizar-ahora")
    public ResponseEntity<String> sincronizarManual() {
        try {
            syncService.sincronizar();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al sincronizar: " + e.getMessage());
        }
        return ResponseEntity.ok("Sincronización disparada manualmente");
    }
}