package com.rochela.rochelasystem.modulos.exportacion.repository;

import com.rochela.rochelasystem.modulos.exportacion.model.VistaEtapasProcesoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VistaEtapasProcesoRepository extends JpaRepository<VistaEtapasProcesoEntity, Long> {

    List<VistaEtapasProcesoEntity> findBySincronizadoSheetsFalse();
}
