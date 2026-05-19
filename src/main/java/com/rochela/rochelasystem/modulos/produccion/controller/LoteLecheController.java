package com.rochela.rochelasystem.modulos.produccion.controller;

import com.rochela.rochelasystem.modulos.produccion.dto.LoteLecheCreateRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.LoteLecheResponse;
import com.rochela.rochelasystem.modulos.produccion.service.LoteLecheService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lotes-leche")
@Tag(name = "Produccion - Lote Leche", description = "Registro de lotes de leche para produccion")
public class LoteLecheController {

    private final LoteLecheService loteLecheService;

    public LoteLecheController(LoteLecheService loteLecheService) {
        this.loteLecheService = loteLecheService;
    }

    @GetMapping
    @Operation(summary = "Listar lotes de leche", description = "Filtra lotes de leche por rango de fechas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de lotes",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = LoteLecheResponse.class))))
    })
    public List<LoteLecheResponse> listar(
            @Parameter(description = "Fecha inicial (YYYY-MM-DD)", example = "2026-05-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @Parameter(description = "Fecha final (YYYY-MM-DD)", example = "2026-05-31")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return loteLecheService.listar(desde, hasta);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de lote de leche", description = "Devuelve el detalle del lote de leche")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lote encontrado",
                    content = @Content(schema = @Schema(implementation = LoteLecheResponse.class))),
            @ApiResponse(responseCode = "404", description = "Lote no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public LoteLecheResponse obtenerDetalle(
            @Parameter(description = "Id del lote de leche", example = "1")
            @PathVariable Long id) {
        return loteLecheService.obtenerDetalle(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar lote de leche", description = "Registra un nuevo lote de leche para produccion")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lote creado",
                    content = @Content(schema = @Schema(implementation = LoteLecheResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del lote de leche",
            required = true,
            content = @Content(schema = @Schema(implementation = LoteLecheCreateRequest.class))
    )
    public LoteLecheResponse crear(@RequestBody LoteLecheCreateRequest request) {
        return loteLecheService.crear(request);
    }
}

