package com.rochela.rochelasystem.modulos.catalogo;

import com.rochela.rochelasystem.modulos.catalogo.model.Producto;
import com.rochela.rochelasystem.modulos.catalogo.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInicializadorCatalogo implements ApplicationRunner {

    private final ProductoRepository productoRepository;
    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        upsertProducto("QF01",   "Queso Fresco 01",    false, false, false);
        upsertProducto("QF01bs", "Queso Fresco 01 BS", false, false, false);
        upsertProducto("SP004",  "SP 004",              true,  true,  false);
        upsertProducto("QF03",   "Queso Fresco 03",     true,  true,  false);
        upsertProducto("QM001",  "Queso Maduro 001",    true,  true,  false);
    }

    private void upsertProducto(String codigo, String nombre,
                                boolean pasteurizacion,
                                boolean cloruro,
                                boolean lavadoDesuerado) {
        if (productoRepository.existsByCodigo(codigo)) return;

        productoRepository.save(
                Producto.builder()
                        .codigo(codigo)
                        .nombre(nombre)
                        .requierePasteurizacion(pasteurizacion)
                        .requiereCloruro(cloruro)
                        .requiereLavadoDesuerado(lavadoDesuerado)
                        .build()
        );
        log.info("Producto creado: {}", codigo);
    }
}
