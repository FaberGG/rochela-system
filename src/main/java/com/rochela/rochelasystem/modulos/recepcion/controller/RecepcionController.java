package com.rochela.rochelasystem.modulos.recepcion.controller;

import com.rochela.rochelasystem.modulos.recepcion.dto.RecepcionCreateRequest;
import com.rochela.rochelasystem.modulos.recepcion.dto.RecepcionPendienteDto;
import com.rochela.rochelasystem.modulos.recepcion.dto.RecepcionReductasaRequest;
import com.rochela.rochelasystem.modulos.recepcion.dto.RecepcionReductasaResponse;
import com.rochela.rochelasystem.modulos.recepcion.dto.RecepcionResponse;
import com.rochela.rochelasystem.modulos.recepcion.model.RecepcionLeche;
import com.rochela.rochelasystem.modulos.recepcion.service.RecepcionService;
import com.rochela.rochelasystem.shared.enums.ResultadoValidacion;
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
@RequestMapping("/api/v1/recepciones")
@Tag(name = "Recepcion", description = "Gestion de recepcion de leche")
public class RecepcionController {

    private final RecepcionService recepcionService;

    public RecepcionController(RecepcionService recepcionService) {
        this.recepcionService = recepcionService;
    }

    @GetMapping
    @Operation(summary = "Listar recepciones", description = "Filtra recepciones por proveedor, fechas y resultado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de recepciones",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = RecepcionLeche.class))))
    })
    public List<RecepcionLeche> listarRecepciones(
            @Parameter(description = "Id del proveedor", example = "10")
            @RequestParam(required = false) Long proveedorId,
            @Parameter(description = "Fecha inicial (YYYY-MM-DD)", example = "2026-05-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @Parameter(description = "Fecha final (YYYY-MM-DD)", example = "2026-05-31")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @Parameter(description = "Resultado de validacion", example = "APTA")
            @RequestParam(required = false) ResultadoValidacion resultado) {
        return recepcionService.listarRecepciones(proveedorId, desde, hasta, resultado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de recepcion", description = "Devuelve el detalle de validaciones")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recepcion encontrada",
                    content = @Content(schema = @Schema(implementation = RecepcionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recepcion no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public RecepcionResponse obtenerDetalle(
            @Parameter(description = "Id de la recepcion", example = "1")
            @PathVariable Long id) {
        return recepcionService.obtenerDetalle(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar recepcion", description = "Registra una nueva recepcion de leche")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Recepcion creada",
                    content = @Content(schema = @Schema(implementation = RecepcionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la recepcion",
            required = true,
            content = @Content(schema = @Schema(implementation = RecepcionCreateRequest.class))
    )
    public RecepcionResponse crear(@RequestBody RecepcionCreateRequest request) {
        return recepcionService.crear(request);
    }

    @PatchMapping("/{id}/reductasa")
    @Operation(summary = "Cerrar reductasa", description = "Registra la hora fin de la prueba de reductasa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reductasa cerrada",
                    content = @Content(schema = @Schema(implementation = RecepcionReductasaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Recepcion no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Operacion invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de cierre de reductasa",
            required = true,
            content = @Content(schema = @Schema(implementation = RecepcionReductasaRequest.class))
    )
    public RecepcionReductasaResponse cerrarReductasa(
            @Parameter(description = "Id de la recepcion", example = "1")
            @PathVariable Long id,
            @RequestBody RecepcionReductasaRequest request) {
        return recepcionService.cerrarReductasa(id, request.getHoraFinReductasa());
    }

    @GetMapping("/pendientes")
    @Operation(summary = "Listar pendientes de reductasa", description = "Lista recepciones con reductasa pendiente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pendientes",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = RecepcionPendienteDto.class))))
    })
    public List<RecepcionPendienteDto> listarPendientes() {
        return recepcionService.listarPendientes();
    }
}

