package com.rochela.rochelasystem.modulos.produccion.repository;

import com.rochela.rochelasystem.modulos.produccion.model.CierreLote;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CierreLoteRepository extends JpaRepository<CierreLote, Long> {

    Optional<CierreLote> findByLoteId(Long loteId);
}

