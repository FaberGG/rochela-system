package com.rochela.rochelasystem.modulos.proveedores.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProveedorResponseDto {

    private Long id;
    private String nombreEmpresa;
    private String nombrePropietario;
    private String nombreMayordomo;
    private String telefono;
    private String correo;
    private String direccion;
    private Boolean activo;
}

