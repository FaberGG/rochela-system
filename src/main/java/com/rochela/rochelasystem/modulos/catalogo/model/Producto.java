package com.rochela.rochelasystem.modulos.catalogo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "producto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "requiere_pasteurizacion", nullable = false)
    private Boolean requierePasteurizacion;

    @Column(name = "requiere_cloruro", nullable = false)
    private Boolean requiereCloruro;

    @Column(name = "requiere_lavado_desuerado", nullable = false)
    private Boolean requiereLavadoDesuerado;
}

