package com.rochela.rochelasystem.modulos.proveedores.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "ProveedorResponse", description = "Representacion de un proveedor")
public class ProveedorResponseDto {

    @Schema(description = "Identificador del proveedor", example = "10")
    private Long id;

    @Schema(description = "Nombre de la empresa", example = "Lacteos del Norte")
    private String nombreEmpresa;

    @Schema(description = "Nombre del propietario", example = "Juan Perez")
    private String nombrePropietario;

    @Schema(description = "Nombre del mayordomo", example = "Carlos Gomez")
    private String nombreMayordomo;

    @Schema(description = "Telefono de contacto", example = "+57 3001234567")
    private String telefono;

    @Schema(description = "Correo de contacto", example = "contacto@lacteos.com")
    private String correo;

    @Schema(description = "Direccion", example = "Km 5 via principal, Vereda La Isla")
    private String direccion;

    @Schema(description = "Indica si el proveedor esta activo", example = "true")
    private Boolean activo;
}
