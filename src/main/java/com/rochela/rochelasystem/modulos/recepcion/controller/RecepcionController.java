package com.rochela.rochelasystem.modulos.recepcion.controller;

import com.rochela.rochelasystem.modulos.recepcion.dto.RecepcionCreateRequest;
import com.rochela.rochelasystem.modulos.recepcion.dto.RecepcionPendienteDto;
import com.rochela.rochelasystem.modulos.recepcion.dto.RecepcionReductasaRequest;
import com.rochela.rochelasystem.modulos.recepcion.dto.RecepcionReductasaResponse;
import com.rochela.rochelasystem.modulos.recepcion.dto.RecepcionResponse;
import com.rochela.rochelasystem.modulos.recepcion.model.RecepcionLeche;
import com.rochela.rochelasystem.modulos.recepcion.service.RecepcionService;
import com.rochela.rochelasystem.shared.enums.ResultadoValidacion;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recepciones")
public class RecepcionController {

    private final RecepcionService recepcionService;

    public RecepcionController(RecepcionService recepcionService) {
        this.recepcionService = recepcionService;
    }

    @GetMapping
    public List<RecepcionLeche> listarRecepciones(
            @RequestParam(required = false) Long proveedorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) ResultadoValidacion resultado) {
        return recepcionService.listarRecepciones(proveedorId, desde, hasta, resultado);
    }

    @GetMapping("/{id}")
    public RecepcionResponse obtenerDetalle(@PathVariable Long id) {
        return recepcionService.obtenerDetalle(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecepcionResponse crear(@RequestBody RecepcionCreateRequest request) {
        return recepcionService.crear(request);
    }

    @PatchMapping("/{id}/reductasa")
    public RecepcionReductasaResponse cerrarReductasa(
            @PathVariable Long id,
            @RequestBody RecepcionReductasaRequest request) {
        return recepcionService.cerrarReductasa(id, request.getHoraFinReductasa());
    }

    @GetMapping("/pendientes")
    public List<RecepcionPendienteDto> listarPendientes() {
        return recepcionService.listarPendientes();
    }
}

