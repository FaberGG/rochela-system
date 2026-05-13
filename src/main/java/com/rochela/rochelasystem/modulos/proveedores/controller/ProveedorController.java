package com.rochela.rochelasystem.modulos.proveedores.controller;

import com.rochela.rochelasystem.modulos.proveedores.dto.ProveedorRequestDto;
import com.rochela.rochelasystem.modulos.proveedores.dto.ProveedorResponseDto;
import com.rochela.rochelasystem.modulos.proveedores.service.ProveedorService;
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
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public List<ProveedorResponseDto> listarActivos() {
        return proveedorService.listarActivos();
    }

    @GetMapping("/{id}")
    public ProveedorResponseDto obtenerPorId(@PathVariable Long id) {
        return proveedorService.obtenerPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProveedorResponseDto crear(@RequestBody ProveedorRequestDto proveedor) {
        return proveedorService.crear(proveedor);
    }

    @PutMapping("/{id}")
    public ProveedorResponseDto actualizar(@PathVariable Long id, @RequestBody ProveedorRequestDto proveedor) {
        return proveedorService.actualizar(id, proveedor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        proveedorService.eliminar(id);
    }
}
