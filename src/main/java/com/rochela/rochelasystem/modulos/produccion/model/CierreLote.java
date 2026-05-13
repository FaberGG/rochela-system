package com.rochela.rochelasystem.modulos.produccion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cierre_lote")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CierreLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lote_id", nullable = false, unique = true)
    private Long loteId;

    @Column(name = "fecha_hora_cierre", nullable = false)
    private LocalDateTime fechaHoraCierre;

    @Column(name = "unidades_producidas", nullable = false)
    private Integer unidadesProducidas;

    @Column(name = "peso_total_kg", nullable = false)
    private Double pesoTotalKg;

    @Column(name = "rendimiento_teorico")
    private Double rendimientoTeorico;

    @Column(name = "rendimiento_general")
    private Double rendimientoGeneral;
}

