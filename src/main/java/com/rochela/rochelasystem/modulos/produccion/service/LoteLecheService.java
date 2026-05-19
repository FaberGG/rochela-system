package com.rochela.rochelasystem.modulos.produccion.service;

import com.rochela.rochelasystem.modulos.produccion.dto.LoteLecheCreateRequest;
import com.rochela.rochelasystem.modulos.produccion.dto.LoteLecheResponse;
import com.rochela.rochelasystem.modulos.produccion.model.Descarga;
import com.rochela.rochelasystem.modulos.produccion.model.LoteLeche;
import com.rochela.rochelasystem.modulos.produccion.repository.DescargaRepository;
import com.rochela.rochelasystem.modulos.produccion.repository.LoteLecheRepository;
import com.rochela.rochelasystem.modulos.recepcion.model.RecepcionLeche;
import com.rochela.rochelasystem.modulos.recepcion.repository.RecepcionLecheRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LoteLecheService {

    private final LoteLecheRepository loteLecheRepository;
    private final DescargaRepository descargaRepository;
    private final RecepcionLecheRepository recepcionLecheRepository;

    public LoteLecheService(LoteLecheRepository loteLecheRepository,
                            DescargaRepository descargaRepository,
                            RecepcionLecheRepository recepcionLecheRepository) {
        this.loteLecheRepository = loteLecheRepository;
        this.descargaRepository = descargaRepository;
        this.recepcionLecheRepository = recepcionLecheRepository;
    }

    @Transactional
    public LoteLecheResponse crear(LoteLecheCreateRequest request) {
        if (request.getRecepcionLecheIds() == null || request.getRecepcionLecheIds().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debe indicar al menos una recepcion de leche.");
        }

        List<Long> recepcionIds = request.getRecepcionLecheIds().stream().distinct().toList();
        List<RecepcionLeche> recepciones = recepcionLecheRepository.findAllById(recepcionIds);
        validarRecepciones(recepcionIds, recepciones);

        double totalLitros = recepciones.stream()
                .map(RecepcionLeche::getCantidadLitros)
                .filter(item -> item != null)
                .mapToDouble(Double::doubleValue)
                .sum();

        LoteLeche loteLeche = LoteLeche.builder()
                .codigoLoteLeche(request.getCodigoLoteLeche())
                .fechaHora(request.getFechaHora() != null ? request.getFechaHora() : LocalDateTime.now())
                .cantidadLitrosTotal(totalLitros)
                .tanqueProceso(request.getTanqueProceso())
                .realizadoPor(request.getRealizadoPor())
                .grasa(request.getGrasa())
                .solidosNoGrasos(request.getSolidosNoGrasos())
                .temperatura(request.getTemperatura())
                .proteina(request.getProteina())
                .puntoCrioscopico(request.getPuntoCrioscopico())
                .densidad(request.getDensidad())
                .lactosa(request.getLactosa())
                .solidosTotales(request.getSolidosTotales())
                .aguaAnadida(request.getAguaAnadida())
                .ph(request.getPh())
                .observaciones(request.getObservaciones())
                .build();

        LoteLeche guardado = loteLecheRepository.save(loteLeche);
        List<Descarga> descargas = new ArrayList<>();
        for (Long recepcionId : recepcionIds) {
            Descarga descarga = new Descarga();
            descarga.setLoteLeche(guardado);
            descarga.setRecepcionLecheId(recepcionId);
            descargas.add(descarga);
        }
        descargaRepository.saveAll(descargas);

        return mapResponse(guardado, recepcionIds);
    }

    @Transactional(readOnly = true)
    public LoteLecheResponse obtenerDetalle(Long id) {
        LoteLeche loteLeche = obtenerEntidad(id);
        List<Long> recepcionIds = descargaRepository.findByLoteLecheId(id)
                .stream()
                .map(Descarga::getRecepcionLecheId)
                .toList();
        return mapResponse(loteLeche, recepcionIds);
    }

    @Transactional(readOnly = true)
    public List<LoteLecheResponse> listar(LocalDate desde, LocalDate hasta) {
        return loteLecheRepository.findAll()
                .stream()
                .filter(lote -> filtraPorFecha(lote.getFechaHora(), desde, hasta))
                .map(lote -> {
                    List<Long> recepcionIds = descargaRepository.findByLoteLecheId(lote.getId())
                            .stream()
                            .map(Descarga::getRecepcionLecheId)
                            .toList();
                    return mapResponse(lote, recepcionIds);
                })
                .toList();
    }

    private void validarRecepciones(List<Long> recepcionIds, List<RecepcionLeche> recepciones) {
        Set<Long> encontrados = recepciones.stream()
                .map(RecepcionLeche::getId)
                .collect(Collectors.toSet());
        if (encontrados.size() != recepcionIds.size()) {
            List<Long> faltantes = recepcionIds.stream()
                    .filter(id -> !encontrados.contains(id))
                    .toList();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Recepciones no encontradas: " + faltantes);
        }
    }

    private boolean filtraPorFecha(LocalDateTime fechaHora, LocalDate desde, LocalDate hasta) {
        if (fechaHora == null) {
            return false;
        }
        LocalDate fecha = fechaHora.toLocalDate();
        if (desde != null && fecha.isBefore(desde)) {
            return false;
        }
        if (hasta != null && fecha.isAfter(hasta)) {
            return false;
        }
        return true;
    }

    private LoteLeche obtenerEntidad(Long id) {
        return loteLecheRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "LoteLeche no encontrado: " + id));
    }

    private LoteLecheResponse mapResponse(LoteLeche loteLeche, List<Long> recepcionIds) {
        return LoteLecheResponse.builder()
                .id(loteLeche.getId())
                .codigoLoteLeche(loteLeche.getCodigoLoteLeche())
                .fechaHora(loteLeche.getFechaHora())
                .cantidadLitrosTotal(loteLeche.getCantidadLitrosTotal())
                .tanqueProceso(loteLeche.getTanqueProceso())
                .realizadoPor(loteLeche.getRealizadoPor())
                .recepcionLecheIds(recepcionIds)
                .grasa(loteLeche.getGrasa())
                .solidosNoGrasos(loteLeche.getSolidosNoGrasos())
                .temperatura(loteLeche.getTemperatura())
                .proteina(loteLeche.getProteina())
                .puntoCrioscopico(loteLeche.getPuntoCrioscopico())
                .densidad(loteLeche.getDensidad())
                .lactosa(loteLeche.getLactosa())
                .solidosTotales(loteLeche.getSolidosTotales())
                .aguaAnadida(loteLeche.getAguaAnadida())
                .ph(loteLeche.getPh())
                .observaciones(loteLeche.getObservaciones())
                .build();
    }
}

