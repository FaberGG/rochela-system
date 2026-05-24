package com.rochela.rochelasystem.modulos.produccion.repository;

import com.rochela.rochelasystem.modulos.produccion.model.etapa.EtapaRegistro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtapaRegistroRepository extends JpaRepository<EtapaRegistro, Long> {

    List<EtapaRegistro> findByLoteIdOrderByFechaHoraRegistroAsc(Long loteId);
    java.util.Optional<EtapaRegistro> findTopByLoteIdOrderByFechaHoraRegistroDesc(Long loteId);
}
