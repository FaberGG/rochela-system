package com.rochela.rochelasystem.modulos.catalogo.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductoResponseDto {

    private Long id;
    private String codigo;
    private String nombre;
    private Boolean requierePasteurizacion;
    private Boolean requiereCloruro;
    private Boolean requiereLavadoDesuerado;
}

