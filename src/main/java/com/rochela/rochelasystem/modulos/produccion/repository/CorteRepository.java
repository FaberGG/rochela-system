package com.rochela.rochelasystem.modulos.produccion.repository;

import com.rochela.rochelasystem.modulos.produccion.model.Corte;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorteRepository extends JpaRepository<Corte, Long> {

    List<Corte> findByLoteIdOrderByNumeroCorteAsc(Long loteId);

    Optional<Corte> findTopByLoteIdOrderByNumeroCorteDesc(Long loteId);

    long countByLoteId(Long loteId);
}

