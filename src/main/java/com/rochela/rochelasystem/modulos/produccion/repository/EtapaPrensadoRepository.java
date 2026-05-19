package com.rochela.rochelasystem.modulos.produccion.repository;

import com.rochela.rochelasystem.modulos.produccion.model.etapa.EtapaPrensado;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtapaPrensadoRepository extends JpaRepository<EtapaPrensado, Long> {

    Optional<EtapaPrensado> findTopByLoteIdOrderByFechaHoraRegistroDesc(Long loteId);
}

