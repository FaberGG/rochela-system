package com.rochela.rochelasystem.modulos.catalogo.repository;

import com.rochela.rochelasystem.modulos.catalogo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigo(String codigo);
}
