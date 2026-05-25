package com.rochela.rochelasystem.modulos.produccion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "lote_leche")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoteLeche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_lote_leche", unique = true, nullable = false)
    private String codigoLoteLeche;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "cantidad_litros_total", nullable = false)
    private Double cantidadLitrosTotal;

    @Column(name = "cantidad_litros_disponibles")
    private Double cantidadLitrosDisponibles;

    @Column(name = "tanque_proceso", nullable = false)
    private String tanqueProceso;

    @Column(name = "realizado_por", nullable = false)
    private String realizadoPor;

    // Pruebas Fisicoquímicas de la mezcla total antes de ir a Quesería
    @Column(name = "grasa")
    private Double grasa;

    @Column(name = "solidos_no_grasos")
    private Double solidosNoGrasos;

    @Column(name = "temperatura")
    private Double temperatura;

    @Column(name = "proteina")
    private Double proteina;

    @Column(name = "punto_crioscopico")
    private Double puntoCrioscopico;

    @Column(name = "densidad")
    private Double densidad;

    @Column(name = "lactosa")
    private Double lactosa;

    @Column(name = "solidos_totales")
    private Double solidosTotales;

    @Column(name = "agua_anadida")
    private Double aguaAnadida;

    @Column(name = "ph")
    private Double ph;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
}
