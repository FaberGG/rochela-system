package com.rochela.rochelasystem.modulos.produccion.model.etapa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "etapa_cloruro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EtapaCloruro extends EtapaRegistro {

    @Column(name = "temperatura", nullable = false)
    private Double temperatura;

    @Column(name = "cantidad_gramos", nullable = false)
    private Double cantidadGramos;

    @Column(name = "lote_cloruro", nullable = false)
    private String loteCloruro;
}

