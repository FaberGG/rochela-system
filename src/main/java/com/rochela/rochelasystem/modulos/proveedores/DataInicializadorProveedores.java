package com.rochela.rochelasystem.modulos.proveedores;

import com.rochela.rochelasystem.modulos.proveedores.model.Proveedor;
import com.rochela.rochelasystem.modulos.proveedores.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
class DataInicializadorProveedores implements ApplicationRunner {

    private final ProveedorRepository proveedorRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        upsertProveedor("ANDRÉS RUTA 1");
        upsertProveedor("ANDRÉS RUTA 2");
        upsertProveedor("EDWIN MAÑANA");
        upsertProveedor("EDWIN TARDE");
        upsertProveedor("ANGIE MAÑANA");
        upsertProveedor("ANGIE TARDE");
        upsertProveedor("Calidad Rochela");
        upsertProveedor("Calidad Regreso");
        upsertProveedor("Caquiona");
        upsertProveedor("Valencia");
    }

    private void upsertProveedor(String nombreEmpresa) {
        if (proveedorRepository.existsByNombreEmpresa(nombreEmpresa)) return;

        proveedorRepository.save(
                new Proveedor(null, nombreEmpresa, null, null, null, null, null, true)
        );
        log.info("Proveedor creado: {}", nombreEmpresa);
    }
}