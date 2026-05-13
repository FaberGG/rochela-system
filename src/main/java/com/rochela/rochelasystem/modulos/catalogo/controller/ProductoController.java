package com.rochela.rochelasystem.modulos.catalogo.controller;

import com.rochela.rochelasystem.modulos.catalogo.dto.ProductoResponseDto;
import com.rochela.rochelasystem.modulos.catalogo.service.ProductoService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<ProductoResponseDto> listarProductos() {
        return productoService.listarProductos();
    }

    @GetMapping("/{codigo}")
    public ProductoResponseDto obtenerPorCodigo(@PathVariable String codigo) {
        return productoService.obtenerPorCodigo(codigo);
    }
}
