package com.rochela.rochelasystem.modulos.produccion.model.etapa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "etapa_prensado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EtapaPrensado extends EtapaRegistro {

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;

    @Column(name = "presion_psi", nullable = false)
    private Double presionPsi;

    @Column(name = "responsable", nullable = false)
    private String responsable;
}

