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
import com.rochela.rochelasystem.shared.exception.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Produccion - Lotes", description = "Gestion de lotes y registro de etapas")
public class LoteController {

    private final LoteService loteService;

    public LoteController(LoteService loteService) {
        this.loteService = loteService;
    }

    @GetMapping
    @Operation(summary = "Listar lotes", description = "Filtra lotes por estado, producto y rango de fechas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de lotes",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = LoteResumenResponse.class))))
    })
    public List<LoteResumenResponse> listarLotes(
            @Parameter(description = "Estado actual del lote", example = "INICIADO")
            @RequestParam(required = false) EstadoLote estado,
            @Parameter(description = "Codigo del producto", example = "QUESO-001")
            @RequestParam(required = false) String productoCodigo,
            @Parameter(description = "Fecha inicial (YYYY-MM-DD)", example = "2026-05-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @Parameter(description = "Fecha final (YYYY-MM-DD)", example = "2026-05-31")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @Parameter(description = "Si es true, solo lotes activos", example = "true")
            @RequestParam(required = false) Boolean soloActivos) {
        return loteService.listarLotes(estado, productoCodigo, desde, hasta, soloActivos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de lote", description = "Devuelve el detalle completo del lote")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalle del lote",
                    content = @Content(schema = @Schema(implementation = LoteDetalleResponse.class))),
            @ApiResponse(responseCode = "404", description = "Lote no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public LoteDetalleResponse obtenerDetalle(
            @Parameter(description = "Id del lote", example = "1")
            @PathVariable Long id) {
        return loteService.obtenerDetalle(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear lote", description = "Crea un nuevo lote de produccion")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lote creado",
                    content = @Content(schema = @Schema(implementation = LoteResumenResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos para crear el lote",
            required = true,
            content = @Content(schema = @Schema(implementation = LoteCreateRequest.class))
    )
    public LoteResumenResponse crear(@RequestBody LoteCreateRequest request) {
        return loteService.crearLote(request);
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar lote", description = "Cancela un lote en proceso")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lote cancelado",
                    content = @Content(schema = @Schema(implementation = LoteResumenResponse.class))),
            @ApiResponse(responseCode = "404", description = "Lote no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Transicion invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public LoteResumenResponse cancelar(
            @Parameter(description = "Id del lote", example = "1")
            @PathVariable Long id) {
        return loteService.cancelarLote(id);
    }

    @PostMapping("/{id}/pasteurizacion")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar pasteurizacion", description = "Registra la etapa de pasteurizacion")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Etapa registrada",
                    content = @Content(schema = @Schema(implementation = LoteResumenResponse.class))),
            @ApiResponse(responseCode = "404", description = "Lote no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Transicion invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de pasteurizacion",
            required = true,
            content = @Content(schema = @Schema(implementation = PasteurizacionRequest.class))
    )
    public LoteResumenResponse registrarPasteurizacion(
            @Parameter(description = "Id del lote", example = "1")
            @PathVariable Long id,
            @RequestBody PasteurizacionRequest request) {
        return loteService.registrarPasteurizacion(id, request);
    }

    @PostMapping("/{id}/cloruro")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar cloruro", description = "Registra la etapa de cloruro")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Etapa registrada",
                    content = @Content(schema = @Schema(implementation = LoteResumenResponse.class))),
            @ApiResponse(responseCode = "404", description = "Lote no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Transicion invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de cloruro",
            required = true,
            content = @Content(schema = @Schema(implementation = CloruroRequest.class))
    )
    public LoteResumenResponse registrarCloruro(
            @Parameter(description = "Id del lote", example = "1")
            @PathVariable Long id,
            @RequestBody CloruroRequest request) {
        return loteService.registrarCloruro(id, request);
    }

    @PostMapping("/{id}/cuajo")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar cuajo", description = "Registra la etapa de cuajo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Etapa registrada",
                    content = @Content(schema = @Schema(implementation = LoteResumenResponse.class))),
            @ApiResponse(responseCode = "404", description = "Lote no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Transicion invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de cuajo",
            required = true,
            content = @Content(schema = @Schema(implementation = CuajoRequest.class))
    )
    public LoteResumenResponse registrarCuajo(
            @Parameter(description = "Id del lote", example = "1")
            @PathVariable Long id,
            @RequestBody CuajoRequest request) {
        return loteService.registrarCuajo(id, request);
    }

    @PostMapping("/{id}/cortes")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Agregar corte", description = "Registra un nuevo corte del lote")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Corte registrado",
                    content = @Content(schema = @Schema(implementation = CorteCreateResponse.class))),
            @ApiResponse(responseCode = "404", description = "Lote no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Transicion invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del corte",
            required = true,
            content = @Content(schema = @Schema(implementation = CorteCreateRequest.class))
    )
    public CorteCreateResponse agregarCorte(
            @Parameter(description = "Id del lote", example = "1")
            @PathVariable Long id,
            @RequestBody CorteCreateRequest request) {
        return loteService.agregarCorte(id, request);
    }

    @PostMapping("/{id}/cortes/cerrar")
    @Operation(summary = "Cerrar cortes", description = "Finaliza la etapa de cortes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cortes cerrados",
                    content = @Content(schema = @Schema(implementation = LoteResumenResponse.class))),
            @ApiResponse(responseCode = "404", description = "Lote no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Transicion invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public LoteResumenResponse cerrarCortes(
            @Parameter(description = "Id del lote", example = "1")
            @PathVariable Long id) {
        return loteService.cerrarCortes(id);
    }

    @PostMapping("/{id}/lavado-desuerado")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar lavado y desuerado", description = "Registra la etapa de lavado y desuerado")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Etapa registrada",
                    content = @Content(schema = @Schema(implementation = LoteResumenResponse.class))),
            @ApiResponse(responseCode = "404", description = "Lote no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Transicion invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de lavado y desuerado",
            required = true,
            content = @Content(schema = @Schema(implementation = LavadoDesueradoRequest.class))
    )
    public LoteResumenResponse registrarLavadoDesuerado(
            @Parameter(description = "Id del lote", example = "1")
            @PathVariable Long id,
            @RequestBody LavadoDesueradoRequest request) {
        return loteService.registrarLavadoDesuerado(id, request);
    }

    @PostMapping("/{id}/desuerado")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar desuerado", description = "Registra la etapa de desuerado")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Etapa registrada",
                    content = @Content(schema = @Schema(implementation = LoteResumenResponse.class))),
            @ApiResponse(responseCode = "404", description = "Lote no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Transicion invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de desuerado",
            required = true,
            content = @Content(schema = @Schema(implementation = DesueradoRequest.class))
    )
    public LoteResumenResponse registrarDesuerado(
            @Parameter(description = "Id del lote", example = "1")
            @PathVariable Long id,
            @RequestBody DesueradoRequest request) {
        return loteService.registrarDesuerado(id, request);
    }

    @PostMapping("/{id}/salado")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar salado", description = "Registra la etapa de salado")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Etapa registrada",
                    content = @Content(schema = @Schema(implementation = LoteResumenResponse.class))),
            @ApiResponse(responseCode = "404", description = "Lote no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Transicion invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de salado",
            required = true,
            content = @Content(schema = @Schema(implementation = SaladoRequest.class))
    )
    public LoteResumenResponse registrarSalado(
            @Parameter(description = "Id del lote", example = "1")
            @PathVariable Long id,
            @RequestBody SaladoRequest request) {
        return loteService.registrarSalado(id, request);
    }

    @PostMapping("/{id}/prensado")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar prensado", description = "Registra la etapa de prensado")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Etapa registrada",
                    content = @Content(schema = @Schema(implementation = LoteResumenResponse.class))),
            @ApiResponse(responseCode = "404", description = "Lote no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Transicion invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de prensado",
            required = true,
            content = @Content(schema = @Schema(implementation = PrensadoRequest.class))
    )
    public LoteResumenResponse registrarPrensado(
            @Parameter(description = "Id del lote", example = "1")
            @PathVariable Long id,
            @RequestBody PrensadoRequest request) {
        return loteService.registrarPrensado(id, request);
    }

    @PostMapping("/{id}/cierre")
    @Operation(summary = "Cerrar lote", description = "Registra el cierre del lote y calcula rendimientos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lote cerrado",
                    content = @Content(schema = @Schema(implementation = CierreLoteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Lote no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Transicion invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de cierre del lote",
            required = true,
            content = @Content(schema = @Schema(implementation = CierreLoteRequest.class))
    )
    public CierreLoteResponse cerrarLote(
            @Parameter(description = "Id del lote", example = "1")
            @PathVariable Long id,
            @RequestBody CierreLoteRequest request) {
        return loteService.cerrarLote(id, request);
    }
}

