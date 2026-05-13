package com.rochela.rochelasystem.modulos.catalogo.service;

import com.rochela.rochelasystem.modulos.catalogo.dto.ProductoResponseDto;
import com.rochela.rochelasystem.modulos.catalogo.model.Producto;
import com.rochela.rochelasystem.modulos.catalogo.repository.ProductoRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDto> listarProductos() {
        return productoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponseDto obtenerPorCodigo(String codigo) {
        Producto producto = productoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado: " + codigo));
        return mapToResponse(producto);
    }

    private ProductoResponseDto mapToResponse(Producto producto) {
        return ProductoResponseDto.builder()
                .id(producto.getId())
                .codigo(producto.getCodigo())
                .nombre(producto.getNombre())
                .requierePasteurizacion(producto.getRequierePasteurizacion())
                .requiereCloruro(producto.getRequiereCloruro())
                .requiereLavadoDesuerado(producto.getRequiereLavadoDesuerado())
                .build();
    }
}
