package com.rochela.rochelasystem.modulos.produccion.service;

import com.rochela.rochelasystem.modulos.catalogo.model.Producto;
import com.rochela.rochelasystem.modulos.catalogo.repository.ProductoRepository;
import com.rochela.rochelasystem.modulos.produccion.dto.*;
import com.rochela.rochelasystem.modulos.produccion.dto.CierreLoteQuesoResponse;
import com.rochela.rochelasystem.modulos.produccion.model.CierreLoteQueso;
import com.rochela.rochelasystem.modulos.produccion.model.Corte;
import com.rochela.rochelasystem.modulos.produccion.model.LoteQueso;
import com.rochela.rochelasystem.modulos.produccion.model.LoteLeche;
import com.rochela.rochelasystem.modulos.produccion.model.etapa.EtapaCloruro;
import com.rochela.rochelasystem.modulos.produccion.model.etapa.EtapaCuajo;
import com.rochela.rochelasystem.modulos.produccion.model.etapa.EtapaDesuerado;
import com.rochela.rochelasystem.modulos.produccion.model.etapa.EtapaLavadoDesuerado;
import com.rochela.rochelasystem.modulos.produccion.model.etapa.EtapaPasteurizacion;
import com.rochela.rochelasystem.modulos.produccion.model.etapa.EtapaPrensado;
import com.rochela.rochelasystem.modulos.produccion.model.etapa.EtapaRegistro;
import com.rochela.rochelasystem.modulos.produccion.model.etapa.EtapaSalado;
import com.rochela.rochelasystem.modulos.produccion.repository.CierreLoteQuesoRepository;
import com.rochela.rochelasystem.modulos.produccion.repository.CorteRepository;
import com.rochela.rochelasystem.modulos.produccion.repository.EtapaRegistroRepository;
import com.rochela.rochelasystem.modulos.produccion.repository.EtapaPrensadoRepository;
import com.rochela.rochelasystem.modulos.produccion.repository.LoteQuesoRepository;
import com.rochela.rochelasystem.modulos.produccion.repository.LoteLecheRepository;
import com.rochela.rochelasystem.modulos.produccion.state.LoteState;
import com.rochela.rochelasystem.modulos.produccion.state.LoteStateContext;
import com.rochela.rochelasystem.modulos.produccion.state.StateResolver;
import com.rochela.rochelasystem.shared.event.LoteQuesoCerradoEvent;
import com.rochela.rochelasystem.shared.enums.EstadoLote;
import com.rochela.rochelasystem.shared.enums.TipoEtapa;
import com.rochela.rochelasystem.shared.exception.EstadoInvalidoException;
import com.rochela.rochelasystem.shared.exception.EtapaNoAplicaException;
import com.rochela.rochelasystem.shared.exception.LoteNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LoteService {

    private final LoteQuesoRepository loteQuesoRepository;
    private final ProductoRepository productoRepository;
    private final LoteLecheRepository loteLecheRepository;
    private final EtapaRegistroRepository etapaRegistroRepository;
    private final EtapaPrensadoRepository etapaPrensadoRepository;
    private final CorteRepository corteRepository;
    private final CierreLoteQuesoRepository cierreLoteQuesoRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final StateResolver stateResolver = new StateResolver();

    public LoteService(LoteQuesoRepository loteQuesoRepository,
                       ProductoRepository productoRepository,
                       LoteLecheRepository loteLecheRepository,
                       EtapaRegistroRepository etapaRegistroRepository,
                       EtapaPrensadoRepository etapaPrensadoRepository,
                       CorteRepository corteRepository,
                       CierreLoteQuesoRepository cierreLoteQuesoRepository,
                       ApplicationEventPublisher eventPublisher) {
        this.loteQuesoRepository = loteQuesoRepository;
        this.productoRepository = productoRepository;
        this.loteLecheRepository = loteLecheRepository;
        this.etapaRegistroRepository = etapaRegistroRepository;
        this.etapaPrensadoRepository = etapaPrensadoRepository;
        this.corteRepository = corteRepository;
        this.cierreLoteQuesoRepository = cierreLoteQuesoRepository;
        this.eventPublisher = eventPublisher;
    }

    // ─── Consultas ────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<LoteResumenResponse> listarLotes(EstadoLote estado,
                                                 String productoCodigo,
                                                 LocalDate desde,
                                                 LocalDate hasta,
                                                 Boolean soloActivos,
                                                 Integer limit) {
        Specification<LoteQueso> spec = (root, query, cb) -> cb.conjunction();
        if (estado != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("estadoActual"), estado));
        }
        if (productoCodigo != null && !productoCodigo.isBlank()) {
            Producto producto = productoRepository.findByCodigo(productoCodigo)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Producto no encontrado: " + productoCodigo));
            spec = spec.and((root, query, cb) -> cb.equal(root.get("productoId"), producto.getId()));
        }
        if (desde != null) {
            LocalDateTime inicio = desde.atStartOfDay();
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("fechaHoraInicio"), inicio));
        }
        if (hasta != null) {
            LocalDateTime finExclusivo = hasta.plusDays(1).atStartOfDay();
            spec = spec.and((root, query, cb) -> cb.lessThan(root.get("fechaHoraInicio"), finExclusivo));
        }
        if (Boolean.TRUE.equals(soloActivos)) {
            spec = spec.and((root, query, cb) -> cb.not(root.get("estadoActual")
                    .in(EstadoLote.FINALIZADO, EstadoLote.CANCELADO)));
        }

        List<LoteQueso> loteQuesos;
        if (limit != null && limit > 0) {
            org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(
                    0, limit, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "fechaHoraInicio"));
            loteQuesos = loteQuesoRepository.findAll(spec, pageable).getContent();
        } else {
            loteQuesos = loteQuesoRepository.findAll(spec);
        }

        Map<Long, Producto> productos = productoRepository.findAllById(
                        loteQuesos.stream().map(LoteQueso::getProductoId).distinct().toList())
                .stream()
                .collect(Collectors.toMap(Producto::getId, Function.identity()));

        return loteQuesos.stream()
                .map(loteQueso -> mapResumen(loteQueso, getProducto(productos, loteQueso.getProductoId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public LoteDetalleResponse obtenerDetalle(Long id) {
        LoteQueso loteQueso = obtenerLote(id);
        Producto producto = obtenerProducto(loteQueso.getProductoId());

        List<EtapaDetalleDto> etapas = etapaRegistroRepository.findByLoteIdOrderByFechaHoraRegistroAsc(id)
                .stream()
                .map(this::mapEtapa)
                .toList();

        List<CorteDetalleDto> cortes = corteRepository.findByLoteIdOrderByNumeroCorteAsc(id)
                .stream()
                .map(corte -> CorteDetalleDto.builder()
                        .numeroCorte(corte.getNumeroCorte())
                        .hora(corte.getHora())
                        .observacion(corte.getObservacion())
                        .build())
                .toList();

        Optional<CierreLoteQueso> cierre = cierreLoteQuesoRepository.findByLoteId(id);

        return LoteDetalleResponse.builder()
                .id(loteQueso.getId())
                .codigoLote(loteQueso.getCodigoLote())
                .producto(mapProducto(producto))
                .fechaHoraInicio(loteQueso.getFechaHoraInicio())
                .fechaVencimiento(loteQueso.getFechaVencimiento())
                .grasa(loteQueso.getGrasa())
                .solidosNoGrasos(loteQueso.getSolidosNoGrasos())
                .proteina(loteQueso.getProteina())
                .puntoCrioscopico(loteQueso.getPuntoCrioscopico())
                .temperatura(loteQueso.getTemperatura())
                .densidad(loteQueso.getDensidad())
                .lactosa(loteQueso.getLactosa())
                .solidosTotales(loteQueso.getSolidosTotales())
                .aguaAnadida(loteQueso.getAguaAnadida())
                .ph(loteQueso.getPh())
                .sales(loteQueso.getSales())
                .estadoActual(loteQueso.getEstadoActual())
                .siguienteEtapa(calcularSiguienteEtapa(loteQueso, producto))
                .etapas(etapas)
                .cortes(cortes)
                .cierre(cierre.map(item -> mapCierre(loteQueso, item)).orElse(null))
                .build();
    }

    @Transactional(readOnly = true)
    public Long obtenerLoteLecheId(Long loteId) {
        LoteQueso loteQueso = obtenerLote(loteId);
        return loteQueso.getLoteLeche() != null ? loteQueso.getLoteLeche().getId() : null;
    }

    // ─── Gestión del lote ─────────────────────────────────────────────────────

    @Transactional
    public LoteResumenResponse crearLote(LoteCreateRequest request) {
        Producto producto = productoRepository.findByCodigo(request.getProductoCodigo())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado: " + request.getProductoCodigo()));

        if (request.getLoteLecheId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debe indicar el lote de leche para crear el lote de queso.");
        }

        LoteLeche loteLeche = loteLecheRepository.findById(request.getLoteLecheId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "LoteLeche no encontrado: " + request.getLoteLecheId()));

        LocalDateTime fechaHoraInicio = request.getFechaHoraInicio() != null
                ? request.getFechaHoraInicio()
                : LocalDateTime.now();
        LocalDate fechaInicio = fechaHoraInicio.toLocalDate();
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaInicio.plusDays(1).atStartOfDay();
        int batchDelDia = (int) loteQuesoRepository.countByFechaHoraInicioBetween(inicio, fin) + 1;

        LoteQueso loteQueso = new LoteQueso();
        loteQueso.setCodigoLote(generarCodigoLote(fechaHoraInicio, batchDelDia));
        loteQueso.setProductoId(producto.getId());
        loteQueso.setLoteLeche(loteLeche);
        loteQueso.setFechaHoraInicio(fechaHoraInicio);
        loteQueso.setFechaVencimiento(fechaInicio.plusDays(30));
        loteQueso.setEstadoActual(EstadoLote.INICIADO);
        loteQueso.setBatchDelDia(batchDelDia);
        loteQueso.setGrasa(request.getGrasa());
        loteQueso.setSolidosNoGrasos(request.getSolidosNoGrasos());
        loteQueso.setProteina(request.getProteina());
        loteQueso.setPuntoCrioscopico(request.getPuntoCrioscopico());
        loteQueso.setTemperatura(request.getTemperatura());
        loteQueso.setDensidad(request.getDensidad());
        loteQueso.setLactosa(request.getLactosa());
        loteQueso.setSolidosTotales(request.getSolidosTotales());
        loteQueso.setAguaAnadida(request.getAguaAnadida());
        loteQueso.setPh(request.getPh());
        loteQueso.setSales(request.getSales());

        LoteQueso guardado = loteQuesoRepository.save(loteQueso);
        return mapResumen(guardado, producto);
    }

    @Transactional
    public LoteResumenResponse cancelarLote(Long id) {
        LoteQueso loteQueso = obtenerLote(id);
        Producto producto = obtenerProducto(loteQueso.getProductoId());
        LoteStateContext context = buildContext(producto);

        try {
            LoteState actual = stateResolver.resolve(loteQueso.getEstadoActual());
            loteQueso.setEstadoActual(actual.cancelar(context).getEstado());
        } catch (IllegalStateException ex) {
            throw new EstadoInvalidoException(ex.getMessage());
        }

        LoteQueso guardado = loteQuesoRepository.save(loteQueso);
        return mapResumen(guardado, producto);
    }

    // ─── Registro de etapas ───────────────────────────────────────────────────

    @Transactional
    public LoteResumenResponse registrarPasteurizacion(Long loteId, PasteurizacionRequest request) {
        LoteQueso loteQueso = obtenerLote(loteId);
        Producto producto = obtenerProducto(loteQueso.getProductoId());
        if (!Boolean.TRUE.equals(producto.getRequierePasteurizacion())) {
            throw new EtapaNoAplicaException("El producto " + producto.getCodigo() + " no requiere pasteurizacion.");
        }
        validarEstado(loteQueso, Set.of(EstadoLote.INICIADO), "registrar etapa PASTEURIZACION");

        EtapaPasteurizacion etapa = new EtapaPasteurizacion();
        etapa.setTemperatura(request.getTemperatura());
        inicializarEtapa(etapa, loteQueso.getId(), TipoEtapa.PASTEURIZACION, request.getHora());
        etapaRegistroRepository.save(etapa);

        avanzarEstado(loteQueso, producto);
        return mapResumen(loteQuesoRepository.save(loteQueso), producto);
    }

    @Transactional
    public LoteResumenResponse registrarCloruro(Long loteId, CloruroRequest request) {
        LoteQueso loteQueso = obtenerLote(loteId);
        Producto producto = obtenerProducto(loteQueso.getProductoId());
        if (!Boolean.TRUE.equals(producto.getRequiereCloruro())) {
            throw new EtapaNoAplicaException("El producto " + producto.getCodigo() + " no requiere cloruro.");
        }

        Set<EstadoLote> permitidos = producto.getRequierePasteurizacion()
                ? Set.of(EstadoLote.PASTEURIZACION)
                : Set.of(EstadoLote.INICIADO);
        validarEstado(loteQueso, permitidos, "registrar etapa CLORURO");

        EtapaCloruro etapa = new EtapaCloruro();
        etapa.setTemperatura(request.getTemperatura());
        etapa.setCantidadGramos(request.getCantidadGramos());
        etapa.setLoteCloruro(request.getLoteCloruro());
        inicializarEtapa(etapa, loteQueso.getId(), TipoEtapa.CLORURO, request.getHora());
        etapaRegistroRepository.save(etapa);

        avanzarEstado(loteQueso, producto);
        return mapResumen(loteQuesoRepository.save(loteQueso), producto);
    }

    @Transactional
    public LoteResumenResponse registrarCuajo(Long loteId, CuajoRequest request) {
        LoteQueso loteQueso = obtenerLote(loteId);
        Producto producto = obtenerProducto(loteQueso.getProductoId());

        Set<EstadoLote> permitidos = resolverEstadosParaCuajo(producto);
        validarEstado(loteQueso, permitidos, "registrar etapa CUAJO");

        EtapaCuajo etapa = new EtapaCuajo();
        etapa.setTemperatura(request.getTemperatura());
        etapa.setCantidadGramos(request.getCantidadGramos());
        etapa.setLoteCuajo(request.getLoteCuajo());
        inicializarEtapa(etapa, loteQueso.getId(), TipoEtapa.CUAJO, request.getHora());
        etapaRegistroRepository.save(etapa);

        // Corregido: usa avanzarEstado igual que todos los demás métodos.
        // El estado actual del loteQueso (INICIADO, PASTEURIZACION o CLORURO) determina
        // el avance correcto a través del patrón State.
        avanzarEstado(loteQueso, producto);
        return mapResumen(loteQuesoRepository.save(loteQueso), producto);
    }

    @Transactional
    public CorteCreateResponse agregarCorte(Long loteId, CorteCreateRequest request) {
        LoteQueso loteQueso = obtenerLote(loteId);
        Producto producto = obtenerProducto(loteQueso.getProductoId());

        validarEstado(loteQueso, Set.of(EstadoLote.CORTES, EstadoLote.CUAJO), "agregar corte");
        if (EstadoLote.CUAJO.equals(loteQueso.getEstadoActual())) {
            avanzarEstado(loteQueso, producto);
            loteQuesoRepository.save(loteQueso);
        }

        int siguienteNumero = corteRepository.findTopByLoteIdOrderByNumeroCorteDesc(loteId)
                .map(corte -> corte.getNumeroCorte() + 1)
                .orElse(1);

        Corte corte = new Corte();
        corte.setLoteId(loteId);
        corte.setNumeroCorte(siguienteNumero);
        corte.setHora(request.getHora());
        corte.setObservacion(request.getObservacion());
        corte.setFechaHoraRegistro(LocalDateTime.now());
        Corte guardado = corteRepository.save(corte);

        long total = corteRepository.countByLoteId(loteId);
        return CorteCreateResponse.builder()
                .numeroCorte(guardado.getNumeroCorte())
                .hora(guardado.getHora())
                .observacion(guardado.getObservacion())
                .totalCortesRegistrados(total)
                .build();
    }

    @Transactional
    public LoteResumenResponse cerrarCortes(Long loteId) {
        LoteQueso loteQueso = obtenerLote(loteId);
        Producto producto = obtenerProducto(loteQueso.getProductoId());
        validarEstado(loteQueso, Set.of(EstadoLote.CORTES), "cerrar etapa de cortes");
        avanzarEstadoDesde(loteQueso, EstadoLote.CORTES, producto);
        return mapResumen(loteQuesoRepository.save(loteQueso), producto);
    }

    @Transactional
    public LoteResumenResponse registrarLavadoDesuerado(Long loteId, LavadoDesueradoRequest request) {
        LoteQueso loteQueso = obtenerLote(loteId);
        Producto producto = obtenerProducto(loteQueso.getProductoId());
        if (!Boolean.TRUE.equals(producto.getRequiereLavadoDesuerado())) {
            throw new EtapaNoAplicaException("El producto " + producto.getCodigo() + " no requiere lavado/desuerado.");
        }
        validarEstado(loteQueso, Set.of(EstadoLote.CORTES_CERRADOS), "registrar etapa LAVADO_DESUERADO");

        EtapaLavadoDesuerado etapa = new EtapaLavadoDesuerado();
        etapa.setLitros(request.getLitros());
        inicializarEtapa(etapa, loteQueso.getId(), TipoEtapa.LAVADO_DESUERADO, request.getHora());
        etapaRegistroRepository.save(etapa);

        avanzarEstadoDesde(loteQueso, EstadoLote.CORTES_CERRADOS, producto);
        return mapResumen(loteQuesoRepository.save(loteQueso), producto);
    }

    @Transactional
    public LoteResumenResponse registrarDesuerado(Long loteId, DesueradoRequest request) {
        LoteQueso loteQueso = obtenerLote(loteId);
        Producto producto = obtenerProducto(loteQueso.getProductoId());
        Set<EstadoLote> permitidos = Boolean.TRUE.equals(producto.getRequiereLavadoDesuerado())
                ? Set.of(EstadoLote.LAVADO_DESUERADO)
                : Set.of(EstadoLote.CORTES_CERRADOS);
        validarEstado(loteQueso, permitidos, "registrar etapa DESUERADO");

        EtapaDesuerado etapa = new EtapaDesuerado();
        etapa.setLitros(request.getLitros());
        inicializarEtapa(etapa, loteQueso.getId(), TipoEtapa.DESUERADO, request.getHora());
        etapaRegistroRepository.save(etapa);

        avanzarEstadoDesde(loteQueso, loteQueso.getEstadoActual(), producto);
        return mapResumen(loteQuesoRepository.save(loteQueso), producto);
    }

    @Transactional
    public LoteResumenResponse registrarSalado(Long loteId, SaladoRequest request) {
        LoteQueso loteQueso = obtenerLote(loteId);
        Producto producto = obtenerProducto(loteQueso.getProductoId());
        validarEstado(loteQueso, Set.of(EstadoLote.DESUERADO), "registrar etapa SALADO");

        EtapaSalado etapa = new EtapaSalado();
        etapa.setTemperatura(request.getTemperatura());
        etapa.setCantidadKg(request.getCantidadKg());
        etapa.setSodioInicial(request.getSodioInicial());
        etapa.setSodioFinal(request.getSodioFinal());
        etapa.setLoteSal(request.getLoteSal());
        inicializarEtapa(etapa, loteQueso.getId(), TipoEtapa.SALADO, request.getHora());
        etapaRegistroRepository.save(etapa);

        avanzarEstadoDesde(loteQueso, EstadoLote.DESUERADO, producto);
        return mapResumen(loteQuesoRepository.save(loteQueso), producto);
    }

    @Transactional
    public LoteResumenResponse registrarPrensado(Long loteId, PrensadoRequest request) {
        LoteQueso loteQueso = obtenerLote(loteId);
        Producto producto = obtenerProducto(loteQueso.getProductoId());
        validarEstado(loteQueso, Set.of(EstadoLote.SALADO), "registrar etapa PRENSADO");

        EtapaPrensado etapa = new EtapaPrensado();
        etapa.setPresionPsi(request.getPresionPsi());
        inicializarEtapa(etapa, loteQueso.getId(), TipoEtapa.PRENSADO, request.getHoraInicio());
        etapaRegistroRepository.save(etapa);

        avanzarEstadoDesde(loteQueso, EstadoLote.SALADO, producto);
        return mapResumen(loteQuesoRepository.save(loteQueso), producto);
    }

    @Transactional
    public LoteResumenResponse cerrarPrensado(Long loteId, PrensadoCierreRequest request) {
        LoteQueso loteQueso = obtenerLote(loteId);
        Producto producto = obtenerProducto(loteQueso.getProductoId());
        validarEstado(loteQueso, Set.of(EstadoLote.PRENSADO_INICIADO), "cerrar etapa PRENSADO");

        EtapaPrensado etapa = etapaPrensadoRepository.findTopByLoteIdOrderByFechaHoraRegistroDesc(loteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No hay prensado iniciado para el lote: " + loteId));

        etapa.setHoraFin(request.getHoraFin());
        etapa.setDuracionMinutos(calcularDuracionMinutos(etapa.getHora(), request.getHoraFin()));
        etapa.setResponsable(request.getResponsable());
        etapaRegistroRepository.save(etapa);

        avanzarEstadoDesde(loteQueso, EstadoLote.PRENSADO_INICIADO, producto);
        return mapResumen(loteQuesoRepository.save(loteQueso), producto);
    }

    // ─── Cierre del lote ──────────────────────────────────────────────────────

    @Transactional
    public CierreLoteQuesoResponse cerrarLote(Long loteId, CierreLoteQuesoRequest request) {
        LoteQueso loteQueso = obtenerLote(loteId);
        validarEstado(loteQueso, Set.of(EstadoLote.PRENSADO), "cerrar loteQueso");
        if (cierreLoteQuesoRepository.findByLoteId(loteId).isPresent()) {
            throw new EstadoInvalidoException("El loteQueso " + loteQueso.getCodigoLote() + " ya tiene cierre registrado.");
        }

        CierreLoteQueso cierre = new CierreLoteQueso();
        cierre.setLoteId(loteId);
        cierre.setFechaHoraCierre(LocalDateTime.now());
        cierre.setUnidadesProducidas(request.getUnidadesProducidas());
        cierre.setPesoTotalKg(request.getPesoTotalKg());
        cierre.setRendimientoGeneral(calcularRendimientoGeneral(request.getPesoTotalKg(), loteQueso.getLoteLeche()));
        cierre.setRendimientoTeorico(null);
        cierreLoteQuesoRepository.save(cierre);

        if (request.getObservaciones() != null) {
            loteQueso.setObservaciones(request.getObservaciones());
        }
        loteQueso.setEstadoActual(EstadoLote.FINALIZADO);
        loteQuesoRepository.save(loteQueso);
        eventPublisher.publishEvent(new LoteQuesoCerradoEvent(loteQueso.getId()));

        return CierreLoteQuesoResponse.builder()
                .codigoLote(loteQueso.getCodigoLote())
                .estadoActual(loteQueso.getEstadoActual())
                .fechaHoraCierre(cierre.getFechaHoraCierre())
                .unidadesProducidas(cierre.getUnidadesProducidas())
                .pesoTotalKg(cierre.getPesoTotalKg())
                .rendimientoTeorico(cierre.getRendimientoTeorico())
                .rendimientoGeneral(cierre.getRendimientoGeneral())
                .build();
    }

    // ─── Métodos privados de apoyo ────────────────────────────────────────────

    private void avanzarEstado(LoteQueso loteQueso, Producto producto) {
        LoteStateContext context = buildContext(producto);
        LoteState actual = stateResolver.resolve(loteQueso.getEstadoActual());
        loteQueso.setEstadoActual(actual.avanzar(context).getEstado());
    }

    private void avanzarEstadoDesde(LoteQueso loteQueso, EstadoLote estado, Producto producto) {
        LoteStateContext context = buildContext(producto);
        LoteState actual = stateResolver.resolve(estado);
        loteQueso.setEstadoActual(actual.avanzar(context).getEstado());
    }

    private LoteStateContext buildContext(Producto producto) {
        return new LoteStateContext(
                Boolean.TRUE.equals(producto.getRequierePasteurizacion()),
                Boolean.TRUE.equals(producto.getRequiereCloruro()),
                Boolean.TRUE.equals(producto.getRequiereLavadoDesuerado()),
                stateResolver
        );
    }

    private Set<EstadoLote> resolverEstadosParaCuajo(Producto producto) {
        boolean requierePasteurizacion = Boolean.TRUE.equals(producto.getRequierePasteurizacion());
        boolean requiereCloruro = Boolean.TRUE.equals(producto.getRequiereCloruro());
        if (requiereCloruro) {
            return Set.of(EstadoLote.CLORURO);
        }
        if (requierePasteurizacion) {
            return Set.of(EstadoLote.PASTEURIZACION);
        }
        return Set.of(EstadoLote.INICIADO);
    }

    private void validarEstado(LoteQueso loteQueso, Set<EstadoLote> permitidos, String accion) {
        if (!permitidos.contains(loteQueso.getEstadoActual())) {
            throw new EstadoInvalidoException("El loteQueso " + loteQueso.getCodigoLote()
                    + " esta en estado " + loteQueso.getEstadoActual() + " y no puede " + accion + ".");
        }
    }

    private EtapaRegistro inicializarEtapa(EtapaRegistro etapa, Long loteId, TipoEtapa tipoEtapa, LocalTime hora) {
        etapa.setLoteId(loteId);
        etapa.setTipoEtapa(tipoEtapa);
        etapa.setHora(hora);
        etapa.setFechaHoraRegistro(LocalDateTime.now());
        return etapa;
    }

    /**
     * Calcula el siguiente estado usando siguiente() en lugar de avanzar(),
     * evitando así que calcularSiguienteEtapa simule una transición real.
     * Los estados terminales (FINALIZADO, CANCELADO) retornan null.
     */
    private EstadoLote calcularSiguienteEtapa(LoteQueso loteQueso, Producto producto) {
        LoteStateContext context = buildContext(producto);
        LoteState state = stateResolver.resolve(loteQueso.getEstadoActual());
        LoteState siguiente = state.siguiente(context);
        return siguiente != null ? siguiente.getEstado() : null;
    }

    private LoteResumenResponse mapResumen(LoteQueso loteQueso, Producto producto) {
        return LoteResumenResponse.builder()
                .id(loteQueso.getId())
                .codigoLote(loteQueso.getCodigoLote())
                .producto(mapProducto(producto))
                .fechaHoraInicio(loteQueso.getFechaHoraInicio())
                .fechaVencimiento(loteQueso.getFechaVencimiento())
                .grasa(loteQueso.getGrasa())
                .solidosNoGrasos(loteQueso.getSolidosNoGrasos())
                .proteina(loteQueso.getProteina())
                .puntoCrioscopico(loteQueso.getPuntoCrioscopico())
                .temperatura(loteQueso.getTemperatura())
                .densidad(loteQueso.getDensidad())
                .lactosa(loteQueso.getLactosa())
                .solidosTotales(loteQueso.getSolidosTotales())
                .aguaAnadida(loteQueso.getAguaAnadida())
                .ph(loteQueso.getPh())
                .sales(loteQueso.getSales())
                .estadoActual(loteQueso.getEstadoActual())
                .siguienteEtapa(calcularSiguienteEtapa(loteQueso, producto))
                .etapaActualInicio(resolverEtapaActualInicio(loteQueso))
                .porcentajeCompletado(calcularPorcentajeCompletado(loteQueso, producto))
                .build();
    }

    private LocalDateTime resolverEtapaActualInicio(LoteQueso loteQueso) {
        EstadoLote estado = loteQueso.getEstadoActual();
        if (estado == EstadoLote.INICIADO) {
            return loteQueso.getFechaHoraInicio();
        }
        if (estado == EstadoLote.FINALIZADO || estado == EstadoLote.CANCELADO) {
            return null;
        }
        return etapaRegistroRepository.findTopByLoteIdOrderByFechaHoraRegistroDesc(loteQueso.getId())
                .map(EtapaRegistro::getFechaHoraRegistro)
                .orElse(null);
    }

    private Double calcularPorcentajeCompletado(LoteQueso loteQueso, Producto producto) {
        if (loteQueso.getEstadoActual() == EstadoLote.CANCELADO) {
            return null;
        }
        List<EstadoLote> flujo = construirFlujo(producto);
        int indice = flujo.indexOf(loteQueso.getEstadoActual());
        if (indice < 0 || flujo.size() < 2) {
            return null;
        }
        return (indice * 100.0) / (flujo.size() - 1);
    }

    private List<EstadoLote> construirFlujo(Producto producto) {
        List<EstadoLote> flujo = new java.util.ArrayList<>();
        flujo.add(EstadoLote.INICIADO);
        if (Boolean.TRUE.equals(producto.getRequierePasteurizacion())) {
            flujo.add(EstadoLote.PASTEURIZACION);
        }
        if (Boolean.TRUE.equals(producto.getRequiereCloruro())) {
            flujo.add(EstadoLote.CLORURO);
        }
        flujo.add(EstadoLote.CUAJO);
        flujo.add(EstadoLote.CORTES);
        flujo.add(EstadoLote.CORTES_CERRADOS);
        if (Boolean.TRUE.equals(producto.getRequiereLavadoDesuerado())) {
            flujo.add(EstadoLote.LAVADO_DESUERADO);
        }
        flujo.add(EstadoLote.DESUERADO);
        flujo.add(EstadoLote.SALADO);
        flujo.add(EstadoLote.PRENSADO_INICIADO);
        flujo.add(EstadoLote.PRENSADO);
        flujo.add(EstadoLote.FINALIZADO);
        return flujo;
    }

    private ProductoResumenDto mapProducto(Producto producto) {
        return ProductoResumenDto.builder()
                .codigo(producto.getCodigo())
                .nombre(producto.getNombre())
                .build();
    }

    private EtapaDetalleDto mapEtapa(EtapaRegistro etapa) {
        EtapaDetalleDto.EtapaDetalleDtoBuilder builder = EtapaDetalleDto.builder()
                .tipoEtapa(etapa.getTipoEtapa())
                .hora(etapa.getHora())
                .fechaHoraRegistro(etapa.getFechaHoraRegistro());

        if (etapa instanceof EtapaPasteurizacion pasteurizacion) {
            builder.temperatura(pasteurizacion.getTemperatura());
        } else if (etapa instanceof EtapaCloruro cloruro) {
            builder.temperatura(cloruro.getTemperatura())
                    .cantidadGramos(cloruro.getCantidadGramos())
                    .loteInsumo(cloruro.getLoteCloruro());
        } else if (etapa instanceof EtapaCuajo cuajo) {
            builder.temperatura(cuajo.getTemperatura())
                    .cantidadGramos(cuajo.getCantidadGramos())
                    .loteInsumo(cuajo.getLoteCuajo());
        } else if (etapa instanceof EtapaLavadoDesuerado lavado) {
            builder.litros(lavado.getLitros());
        } else if (etapa instanceof EtapaDesuerado desuerado) {
            builder.litros(desuerado.getLitros());
        } else if (etapa instanceof EtapaSalado salado) {
            builder.temperatura(salado.getTemperatura())
                    .cantidadKg(salado.getCantidadKg())
                    .sodioInicial(salado.getSodioInicial())
                    .sodioFinal(salado.getSodioFinal())
                    .loteSal(salado.getLoteSal());
        } else if (etapa instanceof EtapaPrensado prensado) {
            builder.horaFin(prensado.getHoraFin())
                    .duracionMinutos(prensado.getDuracionMinutos())
                    .presionPsi(prensado.getPresionPsi())
                    .responsable(prensado.getResponsable());
        }

        return builder.build();
    }

    private CierreLoteQuesoResponse mapCierre(LoteQueso loteQueso, CierreLoteQueso cierre) {
        return CierreLoteQuesoResponse.builder()
                .codigoLote(loteQueso.getCodigoLote())
                .estadoActual(EstadoLote.FINALIZADO)
                .fechaHoraCierre(cierre.getFechaHoraCierre())
                .unidadesProducidas(cierre.getUnidadesProducidas())
                .pesoTotalKg(cierre.getPesoTotalKg())
                .rendimientoTeorico(cierre.getRendimientoTeorico())
                .rendimientoGeneral(cierre.getRendimientoGeneral())
                .build();
    }

    private LoteQueso obtenerLote(Long id) {
        return loteQuesoRepository.findById(id)
                .orElseThrow(() -> new LoteNotFoundException(id));
    }

    private Producto obtenerProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado: " + id));
    }

    private Producto getProducto(Map<Long, Producto> productos, Long id) {
        Producto producto = productos.get(id);
        if (producto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado: " + id);
        }
        return producto;
    }

    private int calcularDuracionMinutos(LocalTime inicio, LocalTime fin) {
        if (inicio == null || fin == null) {
            return 0;
        }
        return (int) java.time.Duration.between(inicio, fin).toMinutes();
    }

    private Double calcularRendimientoGeneral(Double pesoTotalKg, LoteLeche loteLeche) {
        if (pesoTotalKg == null || loteLeche == null || loteLeche.getCantidadLitrosTotal() == null
                || loteLeche.getCantidadLitrosTotal() == 0) {
            return null;
        }
        return (pesoTotalKg / loteLeche.getCantidadLitrosTotal()) * 100;
    }

    private String generarCodigoLote(LocalDateTime fechaHoraInicio, int batchDelDia) {
        int diaDelAno = fechaHoraInicio.getDayOfYear();
        int anno = fechaHoraInicio.getYear() % 100;
        return String.format("L%03d%02d%d", diaDelAno, anno, batchDelDia);
    }
}
