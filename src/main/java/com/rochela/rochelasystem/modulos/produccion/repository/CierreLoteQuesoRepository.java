package com.rochela.rochelasystem.modulos.produccion.repository;

import com.rochela.rochelasystem.modulos.produccion.model.CierreLoteQueso;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CierreLoteQuesoRepository extends JpaRepository<CierreLoteQueso, Long> {

    Optional<CierreLoteQueso> findByLoteId(Long loteId);
}

