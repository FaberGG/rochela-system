package com.rochela.rochelasystem.modulos.catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "ProductoResponse", description = "Representacion de un producto del catalogo")
public class ProductoResponseDto {

    @Schema(description = "Identificador del producto", example = "1")
    private Long id;

    @Schema(description = "Codigo unico del producto", example = "QUESO-001")
    private String codigo;

    @Schema(description = "Nombre comercial del producto", example = "Queso campesino")
    private String nombre;

    @Schema(description = "Indica si requiere pasteurizacion", example = "true")
    private Boolean requierePasteurizacion;

    @Schema(description = "Indica si requiere cloruro", example = "true")
    private Boolean requiereCloruro;

    @Schema(description = "Indica si requiere lavado y desuerado", example = "false")
    private Boolean requiereLavadoDesuerado;
}
