package com.rochela.rochelasystem.modulos.catalogo.controller;

import com.rochela.rochelasystem.modulos.catalogo.dto.ProductoResponseDto;
import com.rochela.rochelasystem.modulos.catalogo.service.ProductoService;
import com.rochela.rochelasystem.shared.exception.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Catalogo - Productos", description = "Consultas del catalogo de productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @Operation(summary = "Listar productos", description = "Retorna todos los productos del catalogo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de productos",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductoResponseDto.class))))
    })
    public List<ProductoResponseDto> listarProductos() {
        return productoService.listarProductos();
    }

    @GetMapping("/{codigo}")
    @Operation(summary = "Obtener producto por codigo", description = "Busca un producto por su codigo unico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(schema = @Schema(implementation = ProductoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ProductoResponseDto obtenerPorCodigo(
            @Parameter(description = "Codigo del producto", example = "QUESO-001")
            @PathVariable String codigo) {
        return productoService.obtenerPorCodigo(codigo);
    }
}
