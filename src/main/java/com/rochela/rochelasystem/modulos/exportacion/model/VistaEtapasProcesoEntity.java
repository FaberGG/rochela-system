package com.rochela.rochelasystem.modulos.exportacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vista_etapas_proceso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VistaEtapasProcesoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_lote_queso")
    private String codigoLoteQueso;

    @Column(name = "producto_nombre")
    private String productoNombre;

    @Column(name = "rendimiento_general")
    private Double rendimientoGeneral;

    @Column(name = "peso_total_kg")
    private Double pesoTotalKg;

    @Column(name = "pasteurizacion_temp")
    private Double pasteurizacionTemp;

    @Column(name = "cloruro_temp")
    private Double cloruroTemp;

    @Column(name = "cloruro_gramos")
    private Double cloruroGramos;

    @Column(name = "cuajo_temp")
    private Double cuajoTemp;

    @Column(name = "cuajo_gramos")
    private Double cuajoGramos;

    @Column(name = "lavado_desuerado_litros")
    private Double lavadoDesueradoLitros;

    @Column(name = "desuerado_litros")
    private Double desueradoLitros;

    @Column(name = "salado_temp")
    private Double saladoTemp;

    @Column(name = "salado_cantidad_kg")
    private Double saladoCantidadKg;

    @Column(name = "salado_sodio_inicial")
    private Double saladoSodioInicial;

    @Column(name = "salado_sodio_final")
    private Double saladoSodioFinal;

    @Column(name = "prensado_presion_psi")
    private Double prensadoPresionPsi;

    @Column(name = "prensado_duracion_minutos")
    private Integer prensadoDuracionMinutos;

    @Column(name = "cantidad_cortes")
    private Integer cantidadCortes;

    @Column(name = "sincronizado_sheets", nullable = false)
    private boolean sincronizadoSheets = false;
}
