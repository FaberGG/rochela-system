package com.rochela.rochelasystem.modulos.produccion.service;

import com.rochela.rochelasystem.modulos.produccion.dto.LoteLecheCreateRequest;
import com.rochela.rochelasystem.modulos.produccion.repository.DescargaRepository;
import com.rochela.rochelasystem.modulos.produccion.repository.LoteLecheRepository;
import com.rochela.rochelasystem.modulos.proveedores.model.Proveedor;
import com.rochela.rochelasystem.modulos.proveedores.repository.ProveedorRepository;
import com.rochela.rochelasystem.modulos.recepcion.dto.RecepcionDisponibleParaLoteDto;
import com.rochela.rochelasystem.modulos.recepcion.model.RecepcionLeche;
import com.rochela.rochelasystem.modulos.recepcion.repository.RecepcionLecheRepository;
import com.rochela.rochelasystem.modulos.recepcion.service.RecepcionService;
import com.rochela.rochelasystem.shared.enums.AnalisisOrganoleptico;
import com.rochela.rochelasystem.shared.enums.EstadoRecepcion;
import com.rochela.rochelasystem.shared.enums.Jornada;
import com.rochela.rochelasystem.shared.enums.ResultadoAlcohol;
import com.rochela.rochelasystem.shared.enums.ResultadoValidacion;
import com.rochela.rochelasystem.shared.enums.UbicacionTanque;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class LoteLecheServiceAvailabilityIntegrationTest {

    @Autowired
    private LoteLecheService loteLecheService;

    @Autowired
    private RecepcionService recepcionService;

    @Autowired
    private RecepcionLecheRepository recepcionLecheRepository;

    @Autowired
    private LoteLecheRepository loteLecheRepository;

    @Autowired
    private DescargaRepository descargaRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Test
    void listarDisponiblesParaLoteLeche_incluye_todas_las_completadas_no_asociadas() {
        Proveedor proveedor = crearProveedor("Lacteos del Norte");
        RecepcionLeche disponible = crearRecepcion(proveedor.getId(), ResultadoValidacion.APTA, EstadoRecepcion.COMPLETA, 150.0, 1L);
        RecepcionLeche noApta = crearRecepcion(proveedor.getId(), ResultadoValidacion.NO_APTA, EstadoRecepcion.COMPLETA, 90.0, 2L);
        RecepcionLeche condicional = crearRecepcion(proveedor.getId(), ResultadoValidacion.CONDICIONAL, EstadoRecepcion.COMPLETA, 110.0, 3L);
        crearRecepcion(proveedor.getId(), ResultadoValidacion.APTA, EstadoRecepcion.PENDIENTE_REDUCTASA, 80.0, 4L);

        loteLecheService.crear(loteRequest(disponible.getId()));

        List<RecepcionDisponibleParaLoteDto> disponibles = recepcionService.listarDisponiblesParaLoteLeche();
        Set<Long> idsDisponibles = disponibles.stream()
                .map(RecepcionDisponibleParaLoteDto::getId)
                .collect(Collectors.toSet());

        assertTrue(idsDisponibles.contains(noApta.getId()));
        assertTrue(idsDisponibles.contains(condicional.getId()));
        assertTrue(idsDisponibles.stream().noneMatch(id -> id.equals(disponible.getId())));
    }

    @Test
    void crear_lote_rechaza_recepcion_ya_asociada_a_otro_lote() {
        Proveedor proveedor = crearProveedor("Lacteos del Norte");
        RecepcionLeche usada = crearRecepcion(proveedor.getId(), ResultadoValidacion.APTA, EstadoRecepcion.COMPLETA, 150.0, 5L);

        long descargasAntes = descargaRepository.count();
        loteLecheService.crear(loteRequest(usada.getId()));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> loteLecheService.crear(loteRequest(usada.getId())));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        assertTrue(String.valueOf(ex.getReason()).contains("ya asignadas"));
        assertEquals(descargasAntes + 1, descargaRepository.count());
    }

    @Test
    void crear_lote_permite_recepcion_no_apta_completada() {
        Proveedor proveedor = crearProveedor("Lacteos del Norte");
        RecepcionLeche noApta = crearRecepcion(proveedor.getId(), ResultadoValidacion.NO_APTA, EstadoRecepcion.COMPLETA, 150.0, 6L);

        long lotesAntes = loteLecheRepository.count();
        loteLecheService.crear(loteRequest(noApta.getId()));

        assertEquals(lotesAntes + 1, loteLecheRepository.count());
    }

    private Proveedor crearProveedor(String nombreEmpresa) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombreEmpresa(nombreEmpresa);
        proveedor.setActivo(true);
        return proveedorRepository.save(proveedor);
    }

    private RecepcionLeche crearRecepcion(Long proveedorId,
                                          ResultadoValidacion resultadoValidacion,
                                          EstadoRecepcion estadoRecepcion,
                                          Double litros,
                                          long offsetDias) {
        RecepcionLeche recepcion = RecepcionLeche.builder()
                .fecha(LocalDate.of(2026, 5, 15).plusDays(offsetDias))
                .fechaHora(LocalDateTime.of(2026, 5, 15, 9, 0).plusDays(offsetDias))
                .proveedorId(proveedorId)
                .jornada(Jornada.AM)
                .ubicacion(UbicacionTanque.TANQUE_1)
                .cantidadLitros(litros)
                .realizadoPor("Operario")
                .analisisOrganoleptico(AnalisisOrganoleptico.CUMPLE)
                .colorCumple(true)
                .olorCumple(true)
                .alcohol(ResultadoAlcohol.NEGATIVO)
                .horaInicioReductasa(LocalTime.of(9, 0))
                .resultadoValidacion(resultadoValidacion)
                .estadoRecepcion(estadoRecepcion)
                .build();

        if (EstadoRecepcion.COMPLETA.equals(estadoRecepcion)) {
            recepcion.setHoraFinReductasa(LocalTime.of(11, 30));
            recepcion.setTiempoReductasaMinutos(150);
        }

        return recepcionLecheRepository.save(recepcion);
    }

    private LoteLecheCreateRequest loteRequest(Long recepcionId) {
        return LoteLecheCreateRequest.builder()
                .codigoLoteLeche("LL-TEST-01")
                .fechaHora(LocalDateTime.of(2026, 5, 18, 8, 30))
                .tanqueProceso("SILO_1")
                .realizadoPor("Operario")
                .recepcionLecheIds(List.of(recepcionId))
                .grasa(3.4)
                .solidosNoGrasos(8.1)
                .temperatura(8.5)
                .proteina(3.2)
                .puntoCrioscopico(-0.52)
                .densidad(1.03)
                .lactosa(4.8)
                .solidosTotales(12.3)
                .aguaAnadida(0.0)
                .ph(6.6)
                .observaciones("Mezcla inicial")
                .build();
    }
}
