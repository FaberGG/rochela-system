package com.rochela.rochelasystem.modulos.operario.controller;

import com.rochela.rochelasystem.modulos.operario.dto.OperarioFeedResponse;
import com.rochela.rochelasystem.modulos.operario.service.OperarioFeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/operario")
@Tag(name = "Operario", description = "Feed de pendientes y lotes activos")
public class OperarioController {

    private final OperarioFeedService operarioFeedService;

    public OperarioController(OperarioFeedService operarioFeedService) {
        this.operarioFeedService = operarioFeedService;
    }

    @GetMapping("/feed")
    @Operation(summary = "Obtener feed de operario", description = "Pendientes de reductasa y lotes activos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Feed de operario",
                    content = @Content(schema = @Schema(implementation = OperarioFeedResponse.class)))
    })
    public OperarioFeedResponse obtenerFeed() {
        return operarioFeedService.obtenerFeed();
    }
}

