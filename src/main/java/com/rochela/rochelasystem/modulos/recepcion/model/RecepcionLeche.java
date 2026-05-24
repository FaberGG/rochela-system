package com.rochela.rochelasystem.modulos.recepcion.model;

import com.rochela.rochelasystem.shared.enums.AnalisisOrganoleptico;
import com.rochela.rochelasystem.shared.enums.EstadoRecepcion;
import com.rochela.rochelasystem.shared.enums.Jornada;
import com.rochela.rochelasystem.shared.enums.ResultadoAlcohol;
import com.rochela.rochelasystem.shared.enums.ResultadoValidacion;
import com.rochela.rochelasystem.shared.enums.UbicacionTanque;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recepcion_leche")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RecepcionLeche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "proveedor_id", nullable = false)
    private Long proveedorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "jornada", nullable = false)
    private Jornada jornada;

    @Enumerated(EnumType.STRING)
    @Column(name = "ubicacion", nullable = false)
    private UbicacionTanque ubicacion;

    @Column(name = "cantidad_litros", nullable = false)
    private Double cantidadLitros;

    @Column(name = "realizado_por", nullable = false)
    private String realizadoPor;

    @Enumerated(EnumType.STRING)
    @Column(name = "analisis_organoleptico", nullable = false)
    private AnalisisOrganoleptico analisisOrganoleptico;

    @Column(name = "color_cumple", nullable = false)
    private Boolean colorCumple;

    @Column(name = "olor_cumple", nullable = false)
    private Boolean olorCumple;

    @Enumerated(EnumType.STRING)
    @Column(name = "alcohol", nullable = false)
    private ResultadoAlcohol alcohol;

    @Column(name = "temperatura")
    private Double temperatura;

    @Column(name = "densidad")
    private Double densidad;

    @Column(name = "ph")
    private Double ph;

    @Column(name = "proteina")
    private Double proteina;

    @Column(name = "grasa")
    private Double grasa;

    @Column(name = "solidos_no_grasos")
    private Double solidosNoGrasos;

    @Column(name = "solidos_totales")
    private Double solidosTotales;

    @Column(name = "acidez_titulable")
    private Double acidezTitulable;

    @Column(name = "lactosa")
    private Double lactosa;

    @Column(name = "agua_anadida")
    private Double aguaAnadida;

    @Column(name = "punto_crioscopico")
    private Double puntoCrioscopico;

    @Column(name = "sales")
    private Double sales;

    @Column(name = "hora_inicio_reductasa", nullable = false)
    private LocalTime horaInicioReductasa;

    @Column(name = "hora_fin_reductasa")
    private LocalTime horaFinReductasa;

    @Column(name = "tiempo_reductasa_minutos")
    private Integer tiempoReductasaMinutos;

    @Enumerated(EnumType.STRING)
    @Column(name = "resultado_validacion", nullable = false)
    private ResultadoValidacion resultadoValidacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_recepcion", nullable = false)
    private EstadoRecepcion estadoRecepcion;

    @Lob
    @Column(name = "observaciones")
    private String observaciones;
}
