package com.rochela.rochelasystem.modulos.produccion.repository;

import com.rochela.rochelasystem.modulos.produccion.model.LoteQueso;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LoteQuesoRepository extends JpaRepository<LoteQueso, Long>, JpaSpecificationExecutor<LoteQueso> {

    long countByFechaHoraInicioBetween(LocalDateTime inicio, LocalDateTime fin);
}
