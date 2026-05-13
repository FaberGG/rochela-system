package com.rochela.rochelasystem.modulos.recepcion.repository;

import com.rochela.rochelasystem.modulos.recepcion.model.RecepcionLeche;
import com.rochela.rochelasystem.shared.enums.EstadoRecepcion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RecepcionLecheRepository extends JpaRepository<RecepcionLeche, Long>, JpaSpecificationExecutor<RecepcionLeche> {

    List<RecepcionLeche> findByEstadoRecepcion(EstadoRecepcion estadoRecepcion);
}
