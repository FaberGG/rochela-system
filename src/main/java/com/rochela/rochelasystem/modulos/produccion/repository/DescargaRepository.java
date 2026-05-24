package com.rochela.rochelasystem.modulos.produccion.repository;

import com.rochela.rochelasystem.modulos.produccion.model.Descarga;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DescargaRepository extends JpaRepository<Descarga, Long> {

    List<Descarga> findByLoteLecheId(Long loteLecheId);

    List<Descarga> findByRecepcionLecheIdIn(Collection<Long> recepcionLecheIds);
}
