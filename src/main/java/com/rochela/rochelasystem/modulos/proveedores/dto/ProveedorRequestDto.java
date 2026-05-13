package com.rochela.rochelasystem.modulos.proveedores.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorRequestDto {

    private String nombreEmpresa;
    private String nombrePropietario;
    private String nombreMayordomo;
    private String telefono;
    private String correo;
    private String direccion;
    private Boolean activo;
}

