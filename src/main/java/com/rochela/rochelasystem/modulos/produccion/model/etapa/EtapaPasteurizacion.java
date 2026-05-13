package com.rochela.rochelasystem.modulos.produccion.model.etapa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "etapa_pasteurizacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EtapaPasteurizacion extends EtapaRegistro {

    @Column(name = "temperatura", nullable = false)
    private Double temperatura;
}

