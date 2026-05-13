package com.rochela.rochelasystem.modulos.proveedores.controller;

import com.rochela.rochelasystem.modulos.proveedores.dto.ProveedorRequestDto;
import com.rochela.rochelasystem.modulos.proveedores.dto.ProveedorResponseDto;
import com.rochela.rochelasystem.modulos.proveedores.service.ProveedorService;
import com.rochela.rochelasystem.shared.exception.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/proveedores")
@Tag(name = "Proveedores", description = "Gestion de proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    @Operation(summary = "Listar proveedores activos", description = "Retorna los proveedores con estado activo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de proveedores activos",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProveedorResponseDto.class))))
    })
    public List<ProveedorResponseDto> listarActivos() {
        return proveedorService.listarActivos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener proveedor por id", description = "Busca un proveedor por su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Proveedor encontrado",
                    content = @Content(schema = @Schema(implementation = ProveedorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ProveedorResponseDto obtenerPorId(
            @Parameter(description = "Id del proveedor", example = "10")
            @PathVariable Long id) {
        return proveedorService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear proveedor", description = "Crea un nuevo proveedor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Proveedor creado",
                    content = @Content(schema = @Schema(implementation = ProveedorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del proveedor",
            required = true,
            content = @Content(schema = @Schema(implementation = ProveedorRequestDto.class))
    )
    public ProveedorResponseDto crear(@RequestBody ProveedorRequestDto proveedor) {
        return proveedorService.crear(proveedor);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar proveedor", description = "Actualiza los datos de un proveedor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Proveedor actualizado",
                    content = @Content(schema = @Schema(implementation = ProveedorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del proveedor",
            required = true,
            content = @Content(schema = @Schema(implementation = ProveedorRequestDto.class))
    )
    public ProveedorResponseDto actualizar(
            @Parameter(description = "Id del proveedor", example = "10")
            @PathVariable Long id,
            @RequestBody ProveedorRequestDto proveedor) {
        return proveedorService.actualizar(id, proveedor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar proveedor", description = "Marca un proveedor como inactivo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Proveedor desactivado"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public void eliminar(
            @Parameter(description = "Id del proveedor", example = "10")
            @PathVariable Long id) {
        proveedorService.eliminar(id);
    }
}
