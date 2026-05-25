package com.rochela.rochelasystem.modulos.exportacion.repository;

import com.rochela.rochelasystem.modulos.exportacion.model.VistaRecepcionEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VistaRecepcionRepository extends JpaRepository<VistaRecepcionEntity, Long> {

    List<VistaRecepcionEntity> findBySincronizadoSheetsFalse();

    Optional<VistaRecepcionEntity> findTopByRecepcionIdOrderByIdDesc(Long recepcionId);
}
