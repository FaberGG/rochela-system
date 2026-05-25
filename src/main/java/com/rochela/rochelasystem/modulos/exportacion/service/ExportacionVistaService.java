package com.rochela.rochelasystem.modulos.exportacion.service;

import com.rochela.rochelasystem.modulos.exportacion.model.VistaEtapasProcesoEntity;
import com.rochela.rochelasystem.modulos.exportacion.model.VistaRecepcionEntity;
import com.rochela.rochelasystem.modulos.exportacion.model.VistaRendimientoEntity;
import com.rochela.rochelasystem.modulos.exportacion.repository.VistaEtapasProcesoRepository;
import com.rochela.rochelasystem.modulos.exportacion.repository.VistaRecepcionRepository;
import com.rochela.rochelasystem.modulos.exportacion.repository.VistaRendimientoRepository;
import com.rochela.rochelasystem.modulos.produccion.dto.CierreLoteQuesoResponse;
import com.rochela.rochelasystem.modulos.produccion.dto.EtapaDetalleDto;
import com.rochela.rochelasystem.modulos.produccion.dto.LoteDetalleResponse;
import com.rochela.rochelasystem.modulos.produccion.dto.LoteLecheResponse;
import com.rochela.rochelasystem.modulos.produccion.service.LoteLecheService;
import com.rochela.rochelasystem.modulos.produccion.service.LoteService;
import com.rochela.rochelasystem.modulos.recepcion.model.RecepcionLeche;
import com.rochela.rochelasystem.modulos.recepcion.service.RecepcionService;
import com.rochela.rochelasystem.shared.enums.TipoEtapa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExportacionVistaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportacionVistaService.class);

    private final LoteService loteService;
    private final LoteLecheService loteLecheService;
    private final RecepcionService recepcionService;
    private final VistaRendimientoRepository vistaRendimientoRepository;
    private final VistaEtapasProcesoRepository vistaEtapasProcesoRepository;
    private final VistaRecepcionRepository vistaRecepcionRepository;

    public ExportacionVistaService(LoteService loteService,
                                   LoteLecheService loteLecheService,
                                   RecepcionService recepcionService,
                                   VistaRendimientoRepository vistaRendimientoRepository,
                                   VistaEtapasProcesoRepository vistaEtapasProcesoRepository,
                                   VistaRecepcionRepository vistaRecepcionRepository) {
        this.loteService = loteService;
        this.loteLecheService = loteLecheService;
        this.recepcionService = recepcionService;
        this.vistaRendimientoRepository = vistaRendimientoRepository;
        this.vistaEtapasProcesoRepository = vistaEtapasProcesoRepository;
        this.vistaRecepcionRepository = vistaRecepcionRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrarRecepcion(Long recepcionId) {
        RecepcionLeche recepcion = recepcionService.obtenerEntidadParaExportacion(recepcionId);

        VistaRecepcionEntity vista = vistaRecepcionRepository.findTopByRecepcionIdOrderByIdDesc(recepcionId)
                .orElseGet(VistaRecepcionEntity::new);
        vista.setRecepcionId(recepcion.getId());
        vista.setFecha(recepcion.getFecha());
        vista.setJornada(recepcion.getJornada() != null ? recepcion.getJornada().name() : null);
        vista.setNombreRuta(recepcion.getUbicacion() != null ? recepcion.getUbicacion().name() : null);
        vista.setNombreRecolector(recepcion.getRealizadoPor());
        vista.setCantidadLitros(recepcion.getCantidadLitros());
        vista.setResultadoValidacion(recepcion.getResultadoValidacion() != null
                ? recepcion.getResultadoValidacion().name()
                : null);
        vista.setEstadoRecepcion(recepcion.getEstadoRecepcion() != null
                ? recepcion.getEstadoRecepcion().name()
                : null);
        vista.setTemperatura(recepcion.getTemperatura());
        vista.setPh(recepcion.getPh());
        vista.setDensidad(recepcion.getDensidad());
        vista.setGrasa(recepcion.getGrasa());
        vista.setProteina(recepcion.getProteina());
        vista.setAcidezTitulable(recepcion.getAcidezTitulable());
        vista.setPuntoCrioscopico(recepcion.getPuntoCrioscopico());
        vista.setTiempoReductasaMinutos(recepcion.getTiempoReductasaMinutos());
        vista.setSincronizadoSheets(false);

        VistaRecepcionEntity guardada = vistaRecepcionRepository.save(vista);
        LOGGER.info("VistaRecepcion guardada con id={} recepcionId={}", guardada.getId(), recepcionId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrarLoteQuesoCerrado(Long loteQuesoId) {
        LoteDetalleResponse detalle = loteService.obtenerDetalle(loteQuesoId);
        Long loteLecheId = loteService.obtenerLoteLecheId(loteQuesoId);
        LoteLecheResponse loteLeche = loteLecheService.obtenerDetalle(loteLecheId);
        CierreLoteQuesoResponse cierre = detalle.getCierre();

        VistaRendimientoEntity rendimiento = new VistaRendimientoEntity();
        Double litrosUsados = detalle.getCantidadLitrosUsados();
        if (litrosUsados == null) {
            litrosUsados = loteLeche.getCantidadLitrosTotal();
        }
        rendimiento.setCodigoLoteQueso(detalle.getCodigoLote());
        rendimiento.setProductoNombre(detalle.getProducto() != null ? detalle.getProducto().getNombre() : null);
        rendimiento.setCodigoLoteLeche(loteLeche.getCodigoLoteLeche());
        rendimiento.setFechaHoraInicio(detalle.getFechaHoraInicio());
        rendimiento.setFechaHoraCierre(cierre != null ? cierre.getFechaHoraCierre() : null);
        rendimiento.setLitrosTotalesLeche(litrosUsados);
        rendimiento.setGrasa(loteLeche.getGrasa());
        rendimiento.setProteina(loteLeche.getProteina());
        rendimiento.setPh(loteLeche.getPh());
        rendimiento.setDensidad(loteLeche.getDensidad());
        rendimiento.setSolidosNoGrasos(loteLeche.getSolidosNoGrasos());
        rendimiento.setSolidosTotales(loteLeche.getSolidosTotales());
        rendimiento.setLactosa(loteLeche.getLactosa());
        rendimiento.setPuntoCrioscopico(loteLeche.getPuntoCrioscopico());
        rendimiento.setTemperatura(loteLeche.getTemperatura());
        rendimiento.setAguaAnadida(loteLeche.getAguaAnadida());
        rendimiento.setUnidadesProducidas(cierre != null ? cierre.getUnidadesProducidas() : null);
        rendimiento.setPesoTotalKg(cierre != null ? cierre.getPesoTotalKg() : null);
        rendimiento.setRendimientoTeorico(cierre != null ? cierre.getRendimientoTeorico() : null);
        rendimiento.setRendimientoGeneral(cierre != null ? cierre.getRendimientoGeneral() : null);
        VistaRendimientoEntity guardado = vistaRendimientoRepository.save(rendimiento);

        VistaEtapasProcesoEntity etapas = construirVistaEtapas(detalle, cierre);
        VistaEtapasProcesoEntity etapasGuardadas = vistaEtapasProcesoRepository.save(etapas);
        LOGGER.info("VistaRendimiento guardada id={}, VistaEtapasProceso guardada id={}, loteQuesoId={}",
                guardado.getId(),
                etapasGuardadas.getId(),
                loteQuesoId);
    }

    private VistaEtapasProcesoEntity construirVistaEtapas(LoteDetalleResponse detalle,
                                                          CierreLoteQuesoResponse cierre) {
        VistaEtapasProcesoEntity entity = new VistaEtapasProcesoEntity();
        entity.setCodigoLoteQueso(detalle.getCodigoLote());
        entity.setProductoNombre(detalle.getProducto() != null ? detalle.getProducto().getNombre() : null);
        entity.setRendimientoGeneral(cierre != null ? cierre.getRendimientoGeneral() : null);
        entity.setPesoTotalKg(cierre != null ? cierre.getPesoTotalKg() : null);
        entity.setCantidadCortes(detalle.getCortes() != null ? detalle.getCortes().size() : null);

        if (detalle.getEtapas() == null) {
            return entity;
        }

        for (EtapaDetalleDto etapa : detalle.getEtapas()) {
            if (etapa.getTipoEtapa() == null) {
                continue;
            }
            if (etapa.getTipoEtapa() == TipoEtapa.PASTEURIZACION) {
                entity.setPasteurizacionTemp(etapa.getTemperatura());
            } else if (etapa.getTipoEtapa() == TipoEtapa.CLORURO) {
                entity.setCloruroTemp(etapa.getTemperatura());
                entity.setCloruroGramos(etapa.getCantidadGramos());
            } else if (etapa.getTipoEtapa() == TipoEtapa.CUAJO) {
                entity.setCuajoTemp(etapa.getTemperatura());
                entity.setCuajoGramos(etapa.getCantidadGramos());
            } else if (etapa.getTipoEtapa() == TipoEtapa.LAVADO_DESUERADO) {
                entity.setLavadoDesueradoLitros(etapa.getLitros());
            } else if (etapa.getTipoEtapa() == TipoEtapa.DESUERADO) {
                entity.setDesueradoLitros(etapa.getLitros());
            } else if (etapa.getTipoEtapa() == TipoEtapa.SALADO) {
                entity.setSaladoTemp(etapa.getTemperatura());
                entity.setSaladoCantidadKg(etapa.getCantidadKg());
                entity.setSaladoSodioInicial(etapa.getSodioInicial());
                entity.setSaladoSodioFinal(etapa.getSodioFinal());
            } else if (etapa.getTipoEtapa() == TipoEtapa.PRENSADO) {
                entity.setPrensadoPresionPsi(etapa.getPresionPsi());
                entity.setPrensadoDuracionMinutos(etapa.getDuracionMinutos());
            }
        }

        return entity;
    }
}
