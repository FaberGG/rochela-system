package com.rochela.rochelasystem.modulos.exportacion.model;

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
@Table(name = "vista_rendimiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VistaRendimientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_lote_queso")
    private String codigoLoteQueso;

    @Column(name = "producto_nombre")
    private String productoNombre;

    @Column(name = "codigo_lote_leche")
    private String codigoLoteLeche;

    @Column(name = "fecha_hora_inicio")
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_hora_cierre")
    private LocalDateTime fechaHoraCierre;

    @Column(name = "litros_totales_leche")
    private Double litrosTotalesLeche;

    @Column(name = "grasa")
    private Double grasa;

    @Column(name = "proteina")
    private Double proteina;

    @Column(name = "ph")
    private Double ph;

    @Column(name = "densidad")
    private Double densidad;

    @Column(name = "solidos_no_grasos")
    private Double solidosNoGrasos;

    @Column(name = "solidos_totales")
    private Double solidosTotales;

    @Column(name = "lactosa")
    private Double lactosa;

    @Column(name = "punto_crioscopico")
    private Double puntoCrioscopico;

    @Column(name = "temperatura")
    private Double temperatura;

    @Column(name = "agua_anadida")
    private Double aguaAnadida;

    @Column(name = "unidades_producidas")
    private Integer unidadesProducidas;

    @Column(name = "peso_total_kg")
    private Double pesoTotalKg;

    @Column(name = "rendimiento_teorico")
    private Double rendimientoTeorico;

    @Column(name = "rendimiento_general")
    private Double rendimientoGeneral;

    @Column(name = "sincronizado_sheets", nullable = false)
    private boolean sincronizadoSheets = false;
}
