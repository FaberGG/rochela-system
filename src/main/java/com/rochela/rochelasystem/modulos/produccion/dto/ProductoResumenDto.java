package com.rochela.rochelasystem.modulos.produccion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "ProductoResumen", description = "Resumen del producto asociado al lote")
public class ProductoResumenDto {

    @Schema(description = "Codigo del producto", example = "QUESO-001")
    private String codigo;

    @Schema(description = "Nombre del producto", example = "Queso campesino")
    private String nombre;
}

