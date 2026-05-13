package com.rochela.rochelasystem.modulos.produccion.service;

import com.rochela.rochelasystem.modulos.catalogo.model.Producto;
import com.rochela.rochelasystem.modulos.catalogo.repository.ProductoRepository;
import com.rochela.rochelasystem.modulos.produccion.dto.CierreLoteRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.CierreLoteResponse;
import com.rochela.rochelasystem.modulos.produccion.dto.CloruroRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.CorteCreateRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.CorteCreateResponse;
import com.rochela.rochelasystem.modulos.produccion.dto.CorteDetalleDto;
import com.rochela.rochelasystem.modulos.produccion.dto.CuajoRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.DesueradoRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.EtapaDetalleDto;
import com.rochela.rochelasystem.modulos.produccion.dto.LavadoDesueradoRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.LoteCreateRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.LoteDetalleResponse;
import com.rochela.rochelasystem.modulos.produccion.dto.LoteResumenResponse;
import com.rochela.rochelasystem.modulos.produccion.dto.PasteurizacionRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.PrensadoRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.ProductoResumenDto;
import com.rochela.rochelasystem.modulos.produccion.dto.SaladoRequest;
import com.rochela.rochelasystem.modulos.produccion.model.CierreLote;
import com.rochela.rochelasystem.modulos.produccion.model.Corte;
import com.rochela.rochelasystem.modulos.produccion.model.Lote;
import com.rochela.rochelasystem.modulos.produccion.model.etapa.EtapaCloruro;
import com.rochela.rochelasystem.modulos.produccion.model.etapa.EtapaCuajo;
import com.rochela.rochelasystem.modulos.produccion.model.etapa.EtapaDesuerado;
import com.rochela.rochelasystem.modulos.produccion.model.etapa.EtapaLavadoDesuerado;
import com.rochela.rochelasystem.modulos.produccion.model.etapa.EtapaPasteurizacion;
import com.rochela.rochelasystem.modulos.produccion.model.etapa.EtapaPrensado;
import com.rochela.rochelasystem.modulos.produccion.model.etapa.EtapaRegistro;
import com.rochela.rochelasystem.modulos.produccion.model.etapa.EtapaSalado;
import com.rochela.rochelasystem.modulos.produccion.repository.CierreLoteRepository;
import com.rochela.rochelasystem.modulos.produccion.repository.CorteRepository;
import com.rochela.rochelasystem.modulos.produccion.repository.EtapaRegistroRepository;
import com.rochela.rochelasystem.modulos.produccion.repository.LoteRepository;
import com.rochela.rochelasystem.modulos.produccion.state.LoteState;
import com.rochela.rochelasystem.modulos.produccion.state.LoteStateContext;
import com.rochela.rochelasystem.modulos.produccion.state.StateResolver;
import com.rochela.rochelasystem.modulos.recepcion.model.RecepcionLeche;
import com.rochela.rochelasystem.modulos.recepcion.repository.RecepcionLecheRepository;
import com.rochela.rochelasystem.shared.enums.EstadoLote;
import com.rochela.rochelasystem.shared.enums.TipoEtapa;
import com.rochela.rochelasystem.shared.exception.EstadoInvalidoException;
import com.rochela.rochelasystem.shared.exception.EtapaNoAplicaException;
import com.rochela.rochelasystem.shared.exception.LoteNotFoundException;
import com.rochela.rochelasystem.shared.exception.RecepcionNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LoteService {

    private final LoteRepository loteRepository;
    private final ProductoRepository productoRepository;
    private final RecepcionLecheRepository recepcionLecheRepository;
    private final EtapaRegistroRepository etapaRegistroRepository;
    private final CorteRepository corteRepository;
    private final CierreLoteRepository cierreLoteRepository;
    private final StateResolver stateResolver = new StateResolver();

    public LoteService(LoteRepository loteRepository,
                       ProductoRepository productoRepository,
                       RecepcionLecheRepository recepcionLecheRepository,
                       EtapaRegistroRepository etapaRegistroRepository,
                       CorteRepository corteRepository,
                       CierreLoteRepository cierreLoteRepository) {
        this.loteRepository = loteRepository;
        this.productoRepository = productoRepository;
        this.recepcionLecheRepository = recepcionLecheRepository;
        this.etapaRegistroRepository = etapaRegistroRepository;
        this.corteRepository = corteRepository;
        this.cierreLoteRepository = cierreLoteRepository;
    }

    @Transactional(readOnly = true)
    public List<LoteResumenResponse> listarLotes(EstadoLote estado,
                                                 String productoCodigo,
                                                 LocalDate desde,
                                                 LocalDate hasta,
                                                 Boolean soloActivos) {
        Specification<Lote> spec = (root, query, cb) -> cb.conjunction();
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

        List<Lote> lotes = loteRepository.findAll(spec);
        Map<Long, Producto> productos = productoRepository.findAllById(
                        lotes.stream().map(Lote::getProductoId).distinct().toList())
                .stream()
                .collect(Collectors.toMap(Producto::getId, Function.identity()));

        return lotes.stream()
                .map(lote -> mapResumen(lote, getProducto(productos, lote.getProductoId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public LoteDetalleResponse obtenerDetalle(Long id) {
        Lote lote = obtenerLote(id);
        Producto producto = obtenerProducto(lote.getProductoId());

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

        Optional<CierreLote> cierre = cierreLoteRepository.findByLoteId(id);

        return LoteDetalleResponse.builder()
                .id(lote.getId())
                .codigoLote(lote.getCodigoLote())
                .producto(mapProducto(producto))
                .fechaHoraInicio(lote.getFechaHoraInicio())
                .fechaVencimiento(lote.getFechaVencimiento())
                .estadoActual(lote.getEstadoActual())
                .siguienteEtapa(calcularSiguienteEtapa(lote, producto))
                .etapas(etapas)
                .cortes(cortes)
                .cierre(cierre.map(item -> mapCierre(lote, item)).orElse(null))
                .build();
    }

    @Transactional
    public LoteResumenResponse crearLote(LoteCreateRequest request) {
        Producto producto = productoRepository.findByCodigo(request.getProductoCodigo())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado: " + request.getProductoCodigo()));

        if (request.getRecepcionLecheId() != null
                && !recepcionLecheRepository.existsById(request.getRecepcionLecheId())) {
            throw new RecepcionNotFoundException(request.getRecepcionLecheId());
        }

        LocalDateTime fechaHoraInicio = request.getFechaHoraInicio() != null
                ? request.getFechaHoraInicio()
                : LocalDateTime.now();
        LocalDate fechaInicio = fechaHoraInicio.toLocalDate();
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaInicio.plusDays(1).atStartOfDay();
        int batchDelDia = (int) loteRepository.countByFechaHoraInicioBetween(inicio, fin) + 1;

        Lote lote = new Lote();
        lote.setCodigoLote(generarCodigoLote(fechaHoraInicio, batchDelDia));
        lote.setProductoId(producto.getId());
        lote.setRecepcionLecheId(request.getRecepcionLecheId());
        lote.setFechaHoraInicio(fechaHoraInicio);
        lote.setFechaVencimiento(fechaInicio.plusDays(30));
        lote.setEstadoActual(EstadoLote.INICIADO);
        lote.setBatchDelDia(batchDelDia);

        Lote guardado = loteRepository.save(lote);
        return mapResumen(guardado, producto);
    }

    @Transactional
    public LoteResumenResponse cancelarLote(Long id) {
        Lote lote = obtenerLote(id);
        Producto producto = obtenerProducto(lote.getProductoId());
        LoteStateContext context = buildContext(producto);

        try {
            LoteState actual = stateResolver.resolve(lote.getEstadoActual());
            lote.setEstadoActual(actual.cancelar(context).getEstado());
        } catch (IllegalStateException ex) {
            throw new EstadoInvalidoException(ex.getMessage());
        }

        Lote guardado = loteRepository.save(lote);
        return mapResumen(guardado, producto);
    }

    @Transactional
    public LoteResumenResponse registrarPasteurizacion(Long loteId, PasteurizacionRequest request) {
        Lote lote = obtenerLote(loteId);
        Producto producto = obtenerProducto(lote.getProductoId());
        if (!Boolean.TRUE.equals(producto.getRequierePasteurizacion())) {
            throw new EtapaNoAplicaException("El producto " + producto.getCodigo() + " no requiere pasteurizacion.");
        }
        validarEstado(lote, Set.of(EstadoLote.INICIADO), "registrar etapa PASTEURIZACION");

        EtapaPasteurizacion etapa = new EtapaPasteurizacion();
        etapa.setTemperatura(request.getTemperatura());
        inicializarEtapa(etapa, lote.getId(), TipoEtapa.PASTEURIZACION, request.getHora());
        etapaRegistroRepository.save(etapa);

        avanzarEstado(lote, producto);
        return mapResumen(loteRepository.save(lote), producto);
    }

    @Transactional
    public LoteResumenResponse registrarCloruro(Long loteId, CloruroRequest request) {
        Lote lote = obtenerLote(loteId);
        Producto producto = obtenerProducto(lote.getProductoId());
        if (!Boolean.TRUE.equals(producto.getRequiereCloruro())) {
            throw new EtapaNoAplicaException("El producto " + producto.getCodigo() + " no requiere cloruro.");
        }

        Set<EstadoLote> permitidos = producto.getRequierePasteurizacion()
                ? Set.of(EstadoLote.PASTEURIZACION)
                : Set.of(EstadoLote.INICIADO);
        validarEstado(lote, permitidos, "registrar etapa CLORURO");

        EtapaCloruro etapa = new EtapaCloruro();
        etapa.setTemperatura(request.getTemperatura());
        etapa.setCantidadGramos(request.getCantidadGramos());
        etapa.setLoteCloruro(request.getLoteCloruro());
        inicializarEtapa(etapa, lote.getId(), TipoEtapa.CLORURO, request.getHora());
        etapaRegistroRepository.save(etapa);

        avanzarEstado(lote, producto);
        return mapResumen(loteRepository.save(lote), producto);
    }

    @Transactional
    public LoteResumenResponse registrarCuajo(Long loteId, CuajoRequest request) {
        Lote lote = obtenerLote(loteId);
        Producto producto = obtenerProducto(lote.getProductoId());

        Set<EstadoLote> permitidos = resolverEstadosParaCuajo(producto);
        validarEstado(lote, permitidos, "registrar etapa CUAJO");

        EtapaCuajo etapa = new EtapaCuajo();
        etapa.setTemperatura(request.getTemperatura());
        etapa.setCantidadGramos(request.getCantidadGramos());
        etapa.setLoteCuajo(request.getLoteCuajo());
        inicializarEtapa(etapa, lote.getId(), TipoEtapa.CUAJO, request.getHora());
        etapaRegistroRepository.save(etapa);

        LoteStateContext context = buildContext(producto);
        lote.setEstadoActual(stateResolver.resolve(EstadoLote.CUAJO).avanzar(context).getEstado());
        return mapResumen(loteRepository.save(lote), producto);
    }

    @Transactional
    public CorteCreateResponse agregarCorte(Long loteId, CorteCreateRequest request) {
        Lote lote = obtenerLote(loteId);
        validarEstado(lote, Set.of(EstadoLote.CORTES), "agregar corte");

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
        Lote lote = obtenerLote(loteId);
        Producto producto = obtenerProducto(lote.getProductoId());
        validarEstado(lote, Set.of(EstadoLote.CORTES), "cerrar etapa de cortes");
        avanzarEstadoDesde(lote, EstadoLote.CORTES, producto);
        return mapResumen(loteRepository.save(lote), producto);
    }

    @Transactional
    public LoteResumenResponse registrarLavadoDesuerado(Long loteId, LavadoDesueradoRequest request) {
        Lote lote = obtenerLote(loteId);
        Producto producto = obtenerProducto(lote.getProductoId());
        if (!Boolean.TRUE.equals(producto.getRequiereLavadoDesuerado())) {
            throw new EtapaNoAplicaException("El producto " + producto.getCodigo() + " no requiere lavado/desuerado.");
        }
        validarEstado(lote, Set.of(EstadoLote.LAVADO_DESUERADO), "registrar etapa LAVADO_DESUERADO");

        EtapaLavadoDesuerado etapa = new EtapaLavadoDesuerado();
        etapa.setLitros(request.getLitros());
        inicializarEtapa(etapa, lote.getId(), TipoEtapa.LAVADO_DESUERADO, request.getHora());
        etapaRegistroRepository.save(etapa);

        avanzarEstadoDesde(lote, EstadoLote.LAVADO_DESUERADO, producto);
        return mapResumen(loteRepository.save(lote), producto);
    }

    @Transactional
    public LoteResumenResponse registrarDesuerado(Long loteId, DesueradoRequest request) {
        Lote lote = obtenerLote(loteId);
        Producto producto = obtenerProducto(lote.getProductoId());
        validarEstado(lote, Set.of(EstadoLote.DESUERADO), "registrar etapa DESUERADO");

        EtapaDesuerado etapa = new EtapaDesuerado();
        etapa.setLitros(request.getLitros());
        inicializarEtapa(etapa, lote.getId(), TipoEtapa.DESUERADO, request.getHora());
        etapaRegistroRepository.save(etapa);

        avanzarEstadoDesde(lote, EstadoLote.DESUERADO, producto);
        return mapResumen(loteRepository.save(lote), producto);
    }

    @Transactional
    public LoteResumenResponse registrarSalado(Long loteId, SaladoRequest request) {
        Lote lote = obtenerLote(loteId);
        Producto producto = obtenerProducto(lote.getProductoId());
        validarEstado(lote, Set.of(EstadoLote.SALADO), "registrar etapa SALADO");

        EtapaSalado etapa = new EtapaSalado();
        etapa.setTemperatura(request.getTemperatura());
        etapa.setCantidadKg(request.getCantidadKg());
        etapa.setSodioInicial(request.getSodioInicial());
        etapa.setSodioFinal(request.getSodioFinal());
        etapa.setLoteSal(request.getLoteSal());
        inicializarEtapa(etapa, lote.getId(), TipoEtapa.SALADO, request.getHora());
        etapaRegistroRepository.save(etapa);

        avanzarEstadoDesde(lote, EstadoLote.SALADO, producto);
        return mapResumen(loteRepository.save(lote), producto);
    }

    @Transactional
    public LoteResumenResponse registrarPrensado(Long loteId, PrensadoRequest request) {
        Lote lote = obtenerLote(loteId);
        Producto producto = obtenerProducto(lote.getProductoId());
        validarEstado(lote, Set.of(EstadoLote.PRENSADO), "registrar etapa PRENSADO");

        EtapaPrensado etapa = new EtapaPrensado();
        etapa.setHoraFin(request.getHoraFin());
        etapa.setDuracionMinutos(calcularDuracionMinutos(request.getHoraInicio(), request.getHoraFin()));
        etapa.setPresionPsi(request.getPresionPsi());
        etapa.setResponsable(request.getResponsable());
        inicializarEtapa(etapa, lote.getId(), TipoEtapa.PRENSADO, request.getHoraInicio());
        etapaRegistroRepository.save(etapa);

        return mapResumen(loteRepository.save(lote), producto);
    }

    @Transactional
    public CierreLoteResponse cerrarLote(Long loteId, CierreLoteRequest request) {
        Lote lote = obtenerLote(loteId);
        validarEstado(lote, Set.of(EstadoLote.PRENSADO), "cerrar lote");
        if (cierreLoteRepository.findByLoteId(loteId).isPresent()) {
            throw new EstadoInvalidoException("El lote " + lote.getCodigoLote() + " ya tiene cierre registrado.");
        }

        CierreLote cierre = new CierreLote();
        cierre.setLoteId(loteId);
        cierre.setFechaHoraCierre(LocalDateTime.now());
        cierre.setUnidadesProducidas(request.getUnidadesProducidas());
        cierre.setPesoTotalKg(request.getPesoTotalKg());
        cierre.setRendimientoGeneral(calcularRendimientoGeneral(request.getPesoTotalKg(), lote.getRecepcionLecheId()));
        cierre.setRendimientoTeorico(null);
        cierreLoteRepository.save(cierre);

        if (request.getObservaciones() != null) {
            lote.setObservaciones(request.getObservaciones());
        }
        lote.setEstadoActual(EstadoLote.FINALIZADO);
        loteRepository.save(lote);

        return CierreLoteResponse.builder()
                .codigoLote(lote.getCodigoLote())
                .estadoActual(lote.getEstadoActual())
                .fechaHoraCierre(cierre.getFechaHoraCierre())
                .unidadesProducidas(cierre.getUnidadesProducidas())
                .pesoTotalKg(cierre.getPesoTotalKg())
                .rendimientoTeorico(cierre.getRendimientoTeorico())
                .rendimientoGeneral(cierre.getRendimientoGeneral())
                .build();
    }

    private void avanzarEstado(Lote lote, Producto producto) {
        LoteStateContext context = buildContext(producto);
        LoteState actual = stateResolver.resolve(lote.getEstadoActual());
        lote.setEstadoActual(actual.avanzar(context).getEstado());
    }

    private void avanzarEstadoDesde(Lote lote, EstadoLote estado, Producto producto) {
        LoteStateContext context = buildContext(producto);
        LoteState actual = stateResolver.resolve(estado);
        lote.setEstadoActual(actual.avanzar(context).getEstado());
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

    private void validarEstado(Lote lote, Set<EstadoLote> permitidos, String accion) {
        if (!permitidos.contains(lote.getEstadoActual())) {
            throw new EstadoInvalidoException("El lote " + lote.getCodigoLote()
                    + " esta en estado " + lote.getEstadoActual() + " y no puede " + accion + ".");
        }
    }

    private EtapaRegistro inicializarEtapa(EtapaRegistro etapa, Long loteId, TipoEtapa tipoEtapa, LocalTime hora) {
        etapa.setLoteId(loteId);
        etapa.setTipoEtapa(tipoEtapa);
        etapa.setHora(hora);
        etapa.setFechaHoraRegistro(LocalDateTime.now());
        return etapa;
    }

    private EstadoLote calcularSiguienteEtapa(Lote lote, Producto producto) {
        try {
            LoteStateContext context = buildContext(producto);
            return stateResolver.resolve(lote.getEstadoActual()).avanzar(context).getEstado();
        } catch (IllegalStateException ex) {
            return null;
        }
    }

    private LoteResumenResponse mapResumen(Lote lote, Producto producto) {
        return LoteResumenResponse.builder()
                .id(lote.getId())
                .codigoLote(lote.getCodigoLote())
                .producto(mapProducto(producto))
                .fechaHoraInicio(lote.getFechaHoraInicio())
                .fechaVencimiento(lote.getFechaVencimiento())
                .estadoActual(lote.getEstadoActual())
                .siguienteEtapa(calcularSiguienteEtapa(lote, producto))
                .build();
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

    private CierreLoteResponse mapCierre(Lote lote, CierreLote cierre) {
        return CierreLoteResponse.builder()
                .codigoLote(lote.getCodigoLote())
                .estadoActual(EstadoLote.FINALIZADO)
                .fechaHoraCierre(cierre.getFechaHoraCierre())
                .unidadesProducidas(cierre.getUnidadesProducidas())
                .pesoTotalKg(cierre.getPesoTotalKg())
                .rendimientoTeorico(cierre.getRendimientoTeorico())
                .rendimientoGeneral(cierre.getRendimientoGeneral())
                .build();
    }

    private Lote obtenerLote(Long id) {
        return loteRepository.findById(id)
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

    private Double calcularRendimientoGeneral(Double pesoTotalKg, Long recepcionLecheId) {
        if (pesoTotalKg == null || recepcionLecheId == null) {
            return null;
        }
        Optional<RecepcionLeche> recepcion = recepcionLecheRepository.findById(recepcionLecheId);
        if (recepcion.isEmpty() || recepcion.get().getCantidadLitros() == null
                || recepcion.get().getCantidadLitros() == 0) {
            return null;
        }
        return (pesoTotalKg / recepcion.get().getCantidadLitros()) * 100;
    }

    private String generarCodigoLote(LocalDateTime fechaHoraInicio, int batchDelDia) {
        int diaDelAno = fechaHoraInicio.getDayOfYear();
        int anno = fechaHoraInicio.getYear() % 100;
        return String.format("L%03d%02d%d", diaDelAno, anno, batchDelDia);
    }
}
