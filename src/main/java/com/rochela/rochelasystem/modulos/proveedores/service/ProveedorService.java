package com.rochela.rochelasystem.modulos.proveedores.service;

import com.rochela.rochelasystem.modulos.proveedores.dto.ProveedorRequestDto;
import com.rochela.rochelasystem.modulos.proveedores.dto.ProveedorResponseDto;
import com.rochela.rochelasystem.modulos.proveedores.model.Proveedor;
import com.rochela.rochelasystem.modulos.proveedores.repository.ProveedorRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Transactional(readOnly = true)
    public List<ProveedorResponseDto> listarActivos() {
        return proveedorRepository.findByActivoTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProveedorResponseDto obtenerPorId(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Proveedor no encontrado: " + id));
        return mapToResponse(proveedor);
    }

    @Transactional
    public ProveedorResponseDto crear(ProveedorRequestDto proveedorDto) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombreEmpresa(proveedorDto.getNombreEmpresa());
        proveedor.setNombrePropietario(proveedorDto.getNombrePropietario());
        proveedor.setNombreMayordomo(proveedorDto.getNombreMayordomo());
        proveedor.setTelefono(proveedorDto.getTelefono());
        proveedor.setCorreo(proveedorDto.getCorreo());
        proveedor.setDireccion(proveedorDto.getDireccion());
        if (proveedorDto.getActivo() == null) {
            proveedor.setActivo(true);
        } else {
            proveedor.setActivo(proveedorDto.getActivo());
        }
        return mapToResponse(proveedorRepository.save(proveedor));
    }

    @Transactional
    public ProveedorResponseDto actualizar(Long id, ProveedorRequestDto cambios) {
        Proveedor existente = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Proveedor no encontrado: " + id));
        existente.setNombreEmpresa(cambios.getNombreEmpresa());
        existente.setNombrePropietario(cambios.getNombrePropietario());
        existente.setNombreMayordomo(cambios.getNombreMayordomo());
        existente.setTelefono(cambios.getTelefono());
        existente.setCorreo(cambios.getCorreo());
        existente.setDireccion(cambios.getDireccion());
        if (cambios.getActivo() != null) {
            existente.setActivo(cambios.getActivo());
        }
        return mapToResponse(proveedorRepository.save(existente));
    }

    @Transactional
    public void eliminar(Long id) {
        Proveedor existente = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Proveedor no encontrado: " + id));
        existente.setActivo(false);
        proveedorRepository.save(existente);
    }

    private ProveedorResponseDto mapToResponse(Proveedor proveedor) {
        return ProveedorResponseDto.builder()
                .id(proveedor.getId())
                .nombreEmpresa(proveedor.getNombreEmpresa())
                .nombrePropietario(proveedor.getNombrePropietario())
                .nombreMayordomo(proveedor.getNombreMayordomo())
                .telefono(proveedor.getTelefono())
                .correo(proveedor.getCorreo())
                .direccion(proveedor.getDireccion())
                .activo(proveedor.getActivo())
                .build();
    }
}
