package com.rochela.rochelasystem.modulos.recepcion.service;

import com.rochela.rochelasystem.modulos.proveedores.service.ProveedorService;
import com.rochela.rochelasystem.modulos.recepcion.dto.RecepcionCreateRequest;
import com.rochela.rochelasystem.modulos.recepcion.dto.RecepcionPendienteDto;
import com.rochela.rochelasystem.modulos.recepcion.dto.RecepcionReductasaResponse;
import com.rochela.rochelasystem.modulos.recepcion.dto.RecepcionResponse;
import com.rochela.rochelasystem.modulos.recepcion.dto.ValidacionDetalleDto;
import com.rochela.rochelasystem.modulos.recepcion.model.RecepcionLeche;
import com.rochela.rochelasystem.modulos.recepcion.repository.RecepcionLecheRepository;
import com.rochela.rochelasystem.shared.enums.EstadoRecepcion;
import com.rochela.rochelasystem.shared.enums.ResultadoValidacion;
import com.rochela.rochelasystem.shared.exception.RecepcionNotFoundException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecepcionService {

    private static final double DENSIDAD_MIN = 1.028;
    private static final double DENSIDAD_MAX = 1.033;
    private static final double PH_MIN = 6.6;
    private static final double PH_MAX = 6.8;
    private static final double ACIDEZ_MIN = 0.16;
    private static final double ACIDEZ_MAX = 0.18;
    private static final double PROTEINA_MIN = 3.5;
    private static final double GRASA_MIN = 4.0;
    private static final double SOLIDOS_TOTALES_MIN = 13.0;
    private static final double AGUA_ANADIDA_MAX = 1.0;
    private static final double PUNTO_CRIOSCOPICO_MIN = 0.53;
    private static final double PUNTO_CRIOSCOPICO_MAX = 0.55;
    private static final long MIN_REDUCTASA_MINUTOS = 180;

    private final RecepcionLecheRepository recepcionLecheRepository;
    private  final ProveedorService proveedorService;

    public RecepcionService(RecepcionLecheRepository recepcionLecheRepository, ProveedorService proveedorService) {
        this.recepcionLecheRepository = recepcionLecheRepository;
        this.proveedorService = proveedorService;
    }

    @Transactional(readOnly = true)
    public List<RecepcionLeche> listarRecepciones(Long proveedorId,
                                                  LocalDate desde,
                                                  LocalDate hasta,
                                                  ResultadoValidacion resultado,
                                                  Integer limit) {
        Specification<RecepcionLeche> spec = (root, query, cb) -> cb.conjunction();
        if (proveedorId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("proveedorId"), proveedorId));
        }
        if (desde != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("fecha"), desde));
        }
        if (hasta != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("fecha"), hasta));
        }
        if (resultado != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("resultadoValidacion"), resultado));
        }
        if (limit != null && limit > 0) {
            org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(
                    0, limit, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "fechaHora"));
            return recepcionLecheRepository.findAll(spec, pageable).getContent();
        }
        return recepcionLecheRepository.findAll(spec);
    }

    @Transactional(readOnly = true)
    public RecepcionResponse obtenerDetalle(Long id) {
        RecepcionLeche recepcion = obtenerEntidad(id);
        Map<String, ValidacionDetalleDto> detalle = construirValidacionDetalle(recepcion);
        return RecepcionResponse.builder()
                .id(recepcion.getId())
                .estadoRecepcion(recepcion.getEstadoRecepcion())
                .resultadoValidacion(recepcion.getResultadoValidacion())
                .validacionDetalle(detalle)
                .build();
    }

    @Transactional
    public RecepcionResponse crear(RecepcionCreateRequest request) {
        RecepcionLeche recepcion = RecepcionLeche.builder()
                .fecha(Objects.requireNonNullElseGet(request.getFecha(), LocalDate::now))
                .fechaHora(LocalDateTime.now())
                .proveedorId(request.getProveedorId())
                .jornada(request.getJornada())
                .ubicacion(request.getUbicacion())
                .cantidadLitros(request.getCantidadLitros())
                .realizadoPor(request.getRealizadoPor())
                .analisisOrganoleptico(request.getAnalisisOrganoleptico())
                .colorCumple(request.getColorCumple())
                .olorCumple(request.getOlorCumple())
                .alcohol(request.getAlcohol())
                .temperatura(request.getTemperatura())
                .densidad(request.getDensidad())
                .ph(request.getPh())
                .proteina(request.getProteina())
                .grasa(request.getGrasa())
                .solidosNoGrasos(request.getSolidosNoGrasos())
                .solidosTotales(request.getSolidosTotales())
                .acidezTitulable(request.getAcidezTitulable())
                .lactosa(request.getLactosa())
                .aguaAnadida(request.getAguaAnadida())
                .puntoCrioscopico(request.getPuntoCrioscopico())
                .sales(request.getSales())
                .horaInicioReductasa(request.getHoraInicioReductasa())
                .observaciones(request.getObservaciones())
                .build();

        Map<String, ValidacionDetalleDto> detalle = construirValidacionDetalle(recepcion);
        ResultadoValidacion resultadoValidacion = calcularResultado(detalle);

        recepcion.setResultadoValidacion(resultadoValidacion);
        recepcion.setEstadoRecepcion(EstadoRecepcion.PENDIENTE_REDUCTASA);

        RecepcionLeche guardada = recepcionLecheRepository.save(recepcion);

        return RecepcionResponse.builder()
                .id(guardada.getId())
                .estadoRecepcion(guardada.getEstadoRecepcion())
                .resultadoValidacion(guardada.getResultadoValidacion())
                .validacionDetalle(detalle)
                .build();
    }

    @Transactional
    public RecepcionReductasaResponse cerrarReductasa(Long id, LocalTime horaFin) {
        RecepcionLeche recepcion = obtenerEntidad(id);
        LocalTime horaInicio = recepcion.getHoraInicioReductasa();
        if (horaInicio == null) {
            throw new IllegalStateException("La recepcion no tiene hora de inicio de reductasa.");
        }
        recepcion.setHoraFinReductasa(horaFin);
        int minutos = (int) Duration.between(horaInicio, horaFin).toMinutes();
        recepcion.setTiempoReductasaMinutos(minutos);
        recepcion.setEstadoRecepcion(EstadoRecepcion.COMPLETA);

        RecepcionLeche guardada = recepcionLecheRepository.save(recepcion);

        boolean cumple = minutos >= MIN_REDUCTASA_MINUTOS;
        return RecepcionReductasaResponse.builder()
                .id(guardada.getId())
                .horaInicioReductasa(guardada.getHoraInicioReductasa())
                .horaFinReductasa(guardada.getHoraFinReductasa())
                .tiempoReductasaMinutos(guardada.getTiempoReductasaMinutos())
                .cumpleReductasa(cumple)
                .estadoRecepcion(guardada.getEstadoRecepcion())
                .resultadoValidacion(guardada.getResultadoValidacion())
                .build();
    }

    @Transactional(readOnly = true)
    public List<RecepcionPendienteDto> listarPendientes() {
        LocalDateTime ahora = LocalDateTime.now();
        return recepcionLecheRepository.findByEstadoRecepcion(EstadoRecepcion.PENDIENTE_REDUCTASA)
                .stream()
                .map(recepcion -> {
                    LocalDate fecha = recepcion.getFecha();
                    LocalTime inicio = recepcion.getHoraInicioReductasa();
                    LocalDateTime inicioDateTime = inicio == null || fecha == null
                            ? ahora
                            : LocalDateTime.of(fecha, inicio);
                    long minutos = Duration.between(inicioDateTime, ahora).toMinutes();
                    return RecepcionPendienteDto.builder()
                            .id(recepcion.getId())
                            .fecha(recepcion.getFecha())
                            .jornada(recepcion.getJornada())
                            .proveedor(proveedorService.obtenerPorId(recepcion.getProveedorId()).getNombreEmpresa())
                            .horaInicioReductasa(recepcion.getHoraInicioReductasa())
                            .minutosTranscurridos(minutos)
                            .fechaHora(recepcion.getFechaHora())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private RecepcionLeche obtenerEntidad(Long id) {
        return recepcionLecheRepository.findById(id)
                .orElseThrow(() -> new RecepcionNotFoundException(id));
    }

    private Map<String, ValidacionDetalleDto> construirValidacionDetalle(RecepcionLeche recepcion) {
        Map<String, ValidacionDetalleDto> detalle = new LinkedHashMap<>();
        agregarValidacion(detalle, "densidad", recepcion.getDensidad(), DENSIDAD_MIN, DENSIDAD_MAX, false);
        agregarValidacion(detalle, "ph", recepcion.getPh(), PH_MIN, PH_MAX, false);
        agregarValidacion(detalle, "acidezTitulable", recepcion.getAcidezTitulable(), ACIDEZ_MIN, ACIDEZ_MAX, false);
        agregarValidacion(detalle, "proteina", recepcion.getProteina(), PROTEINA_MIN, null, true);
        agregarValidacion(detalle, "grasa", recepcion.getGrasa(), GRASA_MIN, null, true);
        agregarValidacion(detalle, "solidosTotales", recepcion.getSolidosTotales(), SOLIDOS_TOTALES_MIN, null, true);
        agregarValidacion(detalle, "aguaAnadida", recepcion.getAguaAnadida(), null, AGUA_ANADIDA_MAX, false);
        agregarValidacion(detalle, "puntoCrioscopico", recepcion.getPuntoCrioscopico(), PUNTO_CRIOSCOPICO_MIN, PUNTO_CRIOSCOPICO_MAX, false);
        return detalle;
    }

    private void agregarValidacion(Map<String, ValidacionDetalleDto> detalle,
                                   String nombre,
                                   Double valor,
                                   Double min,
                                   Double max,
                                   boolean soloMinimo) {
        if (valor == null) {
            return;
        }
        Double comparable = nombre.equals("puntoCrioscopico") ? Math.abs(valor) : valor;
        boolean apto = true;
        if (min != null && comparable < min) {
            apto = false;
        }
        if (max != null && comparable > max) {
            apto = false;
        }
        if (soloMinimo && min != null && comparable < min) {
            apto = false;
        }
        detalle.put(nombre, ValidacionDetalleDto.builder()
                .valor(valor)
                .rangoMin(min)
                .rangoMax(max)
                .apto(apto)
                .build());
    }

    private ResultadoValidacion calcularResultado(Map<String, ValidacionDetalleDto> detalle) {
        boolean hayNoApto = detalle.values().stream().anyMatch(item -> Boolean.FALSE.equals(item.getApto()));
        return hayNoApto ? ResultadoValidacion.NO_APTA : ResultadoValidacion.APTA;
    }
}
