package com.rochela.rochelasystem.modulos.produccion.controller;

import com.rochela.rochelasystem.modulos.produccion.dto.CierreLoteRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.CierreLoteResponse;
import com.rochela.rochelasystem.modulos.produccion.dto.CloruroRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.CorteCreateRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.CorteCreateResponse;
import com.rochela.rochelasystem.modulos.produccion.dto.CuajoRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.DesueradoRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.LavadoDesueradoRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.LoteCreateRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.LoteDetalleResponse;
import com.rochela.rochelasystem.modulos.produccion.dto.LoteResumenResponse;
import com.rochela.rochelasystem.modulos.produccion.dto.PasteurizacionRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.PrensadoRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.SaladoRequest;
import com.rochela.rochelasystem.modulos.produccion.service.LoteService;
import com.rochela.rochelasystem.shared.enums.EstadoLote;
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
@RequestMapping("/api/v1/lotes")
public class LoteController {

    private final LoteService loteService;

    public LoteController(LoteService loteService) {
        this.loteService = loteService;
    }

    @GetMapping
    public List<LoteResumenResponse> listarLotes(
            @RequestParam(required = false) EstadoLote estado,
            @RequestParam(required = false) String productoCodigo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) Boolean soloActivos) {
        return loteService.listarLotes(estado, productoCodigo, desde, hasta, soloActivos);
    }

    @GetMapping("/{id}")
    public LoteDetalleResponse obtenerDetalle(@PathVariable Long id) {
        return loteService.obtenerDetalle(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoteResumenResponse crear(@RequestBody LoteCreateRequest request) {
        return loteService.crearLote(request);
    }

    @PatchMapping("/{id}/cancelar")
    public LoteResumenResponse cancelar(@PathVariable Long id) {
        return loteService.cancelarLote(id);
    }

    @PostMapping("/{id}/pasteurizacion")
    @ResponseStatus(HttpStatus.CREATED)
    public LoteResumenResponse registrarPasteurizacion(@PathVariable Long id,
                                                       @RequestBody PasteurizacionRequest request) {
        return loteService.registrarPasteurizacion(id, request);
    }

    @PostMapping("/{id}/cloruro")
    @ResponseStatus(HttpStatus.CREATED)
    public LoteResumenResponse registrarCloruro(@PathVariable Long id, @RequestBody CloruroRequest request) {
        return loteService.registrarCloruro(id, request);
    }

    @PostMapping("/{id}/cuajo")
    @ResponseStatus(HttpStatus.CREATED)
    public LoteResumenResponse registrarCuajo(@PathVariable Long id, @RequestBody CuajoRequest request) {
        return loteService.registrarCuajo(id, request);
    }

    @PostMapping("/{id}/cortes")
    @ResponseStatus(HttpStatus.CREATED)
    public CorteCreateResponse agregarCorte(@PathVariable Long id, @RequestBody CorteCreateRequest request) {
        return loteService.agregarCorte(id, request);
    }

    @PostMapping("/{id}/cortes/cerrar")
    public LoteResumenResponse cerrarCortes(@PathVariable Long id) {
        return loteService.cerrarCortes(id);
    }

    @PostMapping("/{id}/lavado-desuerado")
    @ResponseStatus(HttpStatus.CREATED)
    public LoteResumenResponse registrarLavadoDesuerado(@PathVariable Long id,
                                                        @RequestBody LavadoDesueradoRequest request) {
        return loteService.registrarLavadoDesuerado(id, request);
    }

    @PostMapping("/{id}/desuerado")
    @ResponseStatus(HttpStatus.CREATED)
    public LoteResumenResponse registrarDesuerado(@PathVariable Long id, @RequestBody DesueradoRequest request) {
        return loteService.registrarDesuerado(id, request);
    }

    @PostMapping("/{id}/salado")
    @ResponseStatus(HttpStatus.CREATED)
    public LoteResumenResponse registrarSalado(@PathVariable Long id, @RequestBody SaladoRequest request) {
        return loteService.registrarSalado(id, request);
    }

    @PostMapping("/{id}/prensado")
    @ResponseStatus(HttpStatus.CREATED)
    public LoteResumenResponse registrarPrensado(@PathVariable Long id, @RequestBody PrensadoRequest request) {
        return loteService.registrarPrensado(id, request);
    }

    @PostMapping("/{id}/cierre")
    public CierreLoteResponse cerrarLote(@PathVariable Long id, @RequestBody CierreLoteRequest request) {
        return loteService.cerrarLote(id, request);
    }
}

