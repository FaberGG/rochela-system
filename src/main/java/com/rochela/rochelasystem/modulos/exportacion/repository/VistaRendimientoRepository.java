package com.rochela.rochelasystem.modulos.exportacion.repository;

import com.rochela.rochelasystem.modulos.exportacion.model.VistaRendimientoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VistaRendimientoRepository extends JpaRepository<VistaRendimientoEntity, Long> {

    List<VistaRendimientoEntity> findBySincronizadoSheetsFalse();
}
