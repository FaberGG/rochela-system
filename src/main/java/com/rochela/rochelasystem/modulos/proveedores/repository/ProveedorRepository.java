package com.rochela.rochelasystem.modulos.proveedores.repository;

import com.rochela.rochelasystem.modulos.proveedores.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    List<Proveedor> findByActivoTrue();
}
