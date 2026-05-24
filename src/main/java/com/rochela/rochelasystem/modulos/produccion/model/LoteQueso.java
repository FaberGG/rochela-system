package com.rochela.rochelasystem.modulos.produccion.model;

import com.rochela.rochelasystem.shared.enums.EstadoLote;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lote")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoteQueso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_lote", nullable = false, unique = true)
    private String codigoLote;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_leche_id", nullable = false)
    private LoteLeche loteLeche;

    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_actual", nullable = false)
    private EstadoLote estadoActual;

    @Column(name = "batch_del_dia", nullable = false)
    private Integer batchDelDia;

    @Column(name = "grasa")
    private Double grasa;

    @Column(name = "solidos_no_grasos")
    private Double solidosNoGrasos;

    @Column(name = "proteina")
    private Double proteina;

    @Column(name = "punto_crioscopico")
    private Double puntoCrioscopico;

    @Column(name = "temperatura")
    private Double temperatura;

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

    @Column(name = "sales")
    private Double sales;

    @Lob
    @Column(name = "observaciones")
    private String observaciones;
}
