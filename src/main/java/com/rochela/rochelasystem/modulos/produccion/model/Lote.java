package com.rochela.rochelasystem.modulos.produccion.model;

import com.rochela.rochelasystem.shared.enums.EstadoLote;
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
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_lote", nullable = false, unique = true)
    private String codigoLote;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(name = "recepcion_leche_id")
    private Long recepcionLecheId;

    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_actual", nullable = false)
    private EstadoLote estadoActual;

    @Column(name = "batch_del_dia", nullable = false)
    private Integer batchDelDia;

    @Lob
    @Column(name = "observaciones")
    private String observaciones;
}

