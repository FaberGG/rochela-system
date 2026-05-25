package com.rochela.rochelasystem.modulos.exportacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vista_recepcion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VistaRecepcionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recepcion_id")
    private Long recepcionId;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "jornada")
    private String jornada;

    @Column(name = "nombre_ruta")
    private String nombreRuta;

    @Column(name = "nombre_recolector")
    private String nombreRecolector;

    @Column(name = "cantidad_litros")
    private Double cantidadLitros;

    @Column(name = "resultado_validacion")
    private String resultadoValidacion;

    @Column(name = "estado_recepcion")
    private String estadoRecepcion;

    @Column(name = "temperatura")
    private Double temperatura;

    @Column(name = "ph")
    private Double ph;

    @Column(name = "densidad")
    private Double densidad;

    @Column(name = "grasa")
    private Double grasa;

    @Column(name = "proteina")
    private Double proteina;

    @Column(name = "acidez_titulable")
    private Double acidezTitulable;

    @Column(name = "punto_crioscopico")
    private Double puntoCrioscopico;

    @Column(name = "tiempo_reductasa_minutos")
    private Integer tiempoReductasaMinutos;

    @Column(name = "sincronizado_sheets", nullable = false)
    private boolean sincronizadoSheets = false;
}
