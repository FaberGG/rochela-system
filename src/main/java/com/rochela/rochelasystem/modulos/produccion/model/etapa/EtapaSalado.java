package com.rochela.rochelasystem.modulos.produccion.model.etapa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "etapa_salado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EtapaSalado extends EtapaRegistro {

    @Column(name = "temperatura", nullable = false)
    private Double temperatura;

    @Column(name = "cantidad_kg", nullable = false)
    private Double cantidadKg;

    @Column(name = "sodio_inicial", nullable = false)
    private Double sodioInicial;

    @Column(name = "sodio_final", nullable = false)
    private Double sodioFinal;

    @Column(name = "lote_sal", nullable = false)
    private String loteSal;
}

