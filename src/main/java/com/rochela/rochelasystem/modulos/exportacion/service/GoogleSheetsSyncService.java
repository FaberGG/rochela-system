package com.rochela.rochelasystem.modulos.exportacion.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.rochela.rochelasystem.modulos.exportacion.model.VistaEtapasProcesoEntity;
import com.rochela.rochelasystem.modulos.exportacion.model.VistaRecepcionEntity;
import com.rochela.rochelasystem.modulos.exportacion.model.VistaRendimientoEntity;
import com.rochela.rochelasystem.modulos.exportacion.repository.VistaEtapasProcesoRepository;
import com.rochela.rochelasystem.modulos.exportacion.repository.VistaRecepcionRepository;
import com.rochela.rochelasystem.modulos.exportacion.repository.VistaRendimientoRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class GoogleSheetsSyncService {

    private static final List<Object> HEADERS_RENDIMIENTO = List.of(
            "Codigo Lote Queso",
            "Producto",
            "Codigo Lote Leche",
            "Fecha Hora Inicio",
            "Fecha Hora Cierre",
            "Litros Usados Leche",
            "Grasa",
            "Proteina",
            "PH",
            "Densidad",
            "Solidos No Grasos",
            "Solidos Totales",
            "Lactosa",
            "Punto Crioscopico",
            "Temperatura",
            "Agua Anadida",
            "Unidades Producidas",
            "Peso Total Kg",
            "Rendimiento Teorico",
            "Rendimiento General"
    );

    private static final List<Object> HEADERS_RECEPCIONES = List.of(
            "Recepcion Id",
            "Fecha",
            "Jornada",
            "Nombre Ruta",
            "Nombre Recolector",
            "Cantidad Litros",
            "Resultado Validacion",
            "Estado Recepcion",
            "Temperatura",
            "PH",
            "Densidad",
            "Grasa",
            "Proteina",
            "Acidez Titulable",
            "Punto Crioscopico",
            "Tiempo Reductasa Minutos"
    );

    private static final List<Object> HEADERS_ETAPAS = List.of(
            "Codigo Lote Queso",
            "Producto",
            "Rendimiento General",
            "Peso Total Kg",
            "Pasteurizacion Temp",
            "Cloruro Temp",
            "Cloruro Gramos",
            "Cuajo Temp",
            "Cuajo Gramos",
            "Lavado Desuerado Litros",
            "Desuerado Litros",
            "Salado Temp",
            "Salado Cantidad Kg",
            "Salado Sodio Inicial",
            "Salado Sodio Final",
            "Prensado Presion Psi",
            "Prensado Duracion Minutos",
            "Cantidad Cortes"
    );

    private final Sheets sheets;
    private final VistaRendimientoRepository vistaRendimientoRepository;
    private final VistaRecepcionRepository vistaRecepcionRepository;
    private final VistaEtapasProcesoRepository vistaEtapasProcesoRepository;

    @Value("${exportacion.google.spreadsheet-id}")
    private String spreadsheetId;

    @Value("${exportacion.google.sheets.rendimiento}")
    private String sheetRendimiento;

    @Value("${exportacion.google.sheets.recepciones}")
    private String sheetRecepciones;

    @Value("${exportacion.google.sheets.etapas}")
    private String sheetEtapas;

    public GoogleSheetsSyncService(Sheets sheets,
                                   VistaRendimientoRepository vistaRendimientoRepository,
                                   VistaRecepcionRepository vistaRecepcionRepository,
                                   VistaEtapasProcesoRepository vistaEtapasProcesoRepository) {
        this.sheets = sheets;
        this.vistaRendimientoRepository = vistaRendimientoRepository;
        this.vistaRecepcionRepository = vistaRecepcionRepository;
        this.vistaEtapasProcesoRepository = vistaEtapasProcesoRepository;
    }

    @Async
    @Scheduled(cron = "0 0 12,18 * * *")
    public void sincronizar() throws IOException {
        sincronizarRendimiento();
        sincronizarRecepciones();
        sincronizarEtapas();
    }

    private void sincronizarRendimiento() throws IOException {
        List<VistaRendimientoEntity> pendientes = vistaRendimientoRepository.findBySincronizadoSheetsFalse();
        if (pendientes.isEmpty()) {
            return;
        }

        List<List<Object>> rows = new ArrayList<>();
        for (VistaRendimientoEntity item : pendientes) {
            rows.add(rowOf(
                    item.getCodigoLoteQueso(),
                    item.getProductoNombre(),
                    item.getCodigoLoteLeche(),
                    toText(item.getFechaHoraInicio()),
                    toText(item.getFechaHoraCierre()),
                    item.getLitrosTotalesLeche(),
                    item.getGrasa(),
                    item.getProteina(),
                    item.getPh(),
                    item.getDensidad(),
                    item.getSolidosNoGrasos(),
                    item.getSolidosTotales(),
                    item.getLactosa(),
                    item.getPuntoCrioscopico(),
                    item.getTemperatura(),
                    item.getAguaAnadida(),
                    item.getUnidadesProducidas(),
                    item.getPesoTotalKg(),
                    item.getRendimientoTeorico(),
                    item.getRendimientoGeneral()
            ));
        }

        List<List<Object>> payload = withHeaderIfEmpty(sheetRendimiento, rows, HEADERS_RENDIMIENTO);
        appendRows(sheetRendimiento, payload);
        pendientes.forEach(item -> item.setSincronizadoSheets(true));
        vistaRendimientoRepository.saveAll(pendientes);
    }

    private void sincronizarRecepciones() throws IOException {
        List<VistaRecepcionEntity> pendientes = vistaRecepcionRepository.findBySincronizadoSheetsFalse();
        if (pendientes.isEmpty()) {
            return;
        }

        List<List<Object>> rows = new ArrayList<>();
        for (VistaRecepcionEntity item : pendientes) {
            rows.add(rowOf(
                    item.getRecepcionId(),
                    toText(item.getFecha()),
                    item.getJornada(),
                    item.getNombreRuta(),
                    item.getNombreRecolector(),
                    item.getCantidadLitros(),
                    item.getResultadoValidacion(),
                    item.getEstadoRecepcion(),
                    item.getTemperatura(),
                    item.getPh(),
                    item.getDensidad(),
                    item.getGrasa(),
                    item.getProteina(),
                    item.getAcidezTitulable(),
                    item.getPuntoCrioscopico(),
                    item.getTiempoReductasaMinutos()
            ));
        }

        List<List<Object>> payload = withHeaderIfEmpty(sheetRecepciones, rows, HEADERS_RECEPCIONES);
        appendRows(sheetRecepciones, payload);
        pendientes.forEach(item -> item.setSincronizadoSheets(true));
        vistaRecepcionRepository.saveAll(pendientes);
    }

    private void sincronizarEtapas() throws IOException {
        List<VistaEtapasProcesoEntity> pendientes = vistaEtapasProcesoRepository.findBySincronizadoSheetsFalse();
        if (pendientes.isEmpty()) {
            return;
        }

        List<List<Object>> rows = new ArrayList<>();
        for (VistaEtapasProcesoEntity item : pendientes) {
            rows.add(rowOf(
                    item.getCodigoLoteQueso(),
                    item.getProductoNombre(),
                    item.getRendimientoGeneral(),
                    item.getPesoTotalKg(),
                    item.getPasteurizacionTemp(),
                    item.getCloruroTemp(),
                    item.getCloruroGramos(),
                    item.getCuajoTemp(),
                    item.getCuajoGramos(),
                    item.getLavadoDesueradoLitros(),
                    item.getDesueradoLitros(),
                    item.getSaladoTemp(),
                    item.getSaladoCantidadKg(),
                    item.getSaladoSodioInicial(),
                    item.getSaladoSodioFinal(),
                    item.getPrensadoPresionPsi(),
                    item.getPrensadoDuracionMinutos(),
                    item.getCantidadCortes()
            ));
        }

        List<List<Object>> payload = withHeaderIfEmpty(sheetEtapas, rows, HEADERS_ETAPAS);
        appendRows(sheetEtapas, payload);
        pendientes.forEach(item -> item.setSincronizadoSheets(true));
        vistaEtapasProcesoRepository.saveAll(pendientes);
    }

    private void appendRows(String sheetName, List<List<Object>> rows) throws IOException {
        if (rows.isEmpty()) {
            return;
        }
        String range = "'" + sheetName + "'!A1";
        ValueRange body = new ValueRange().setValues(rows);
        sheets.spreadsheets().values()
                .append(spreadsheetId, range, body)
                .setValueInputOption("RAW")
                .setInsertDataOption("INSERT_ROWS")
                .execute();
    }

    private String toText(Object value) {
        return value != null ? value.toString() : null;
    }

    private List<Object> rowOf(Object... values) {
        return Arrays.asList(values);
    }

    private List<List<Object>> withHeaderIfEmpty(String sheetName,
                                                 List<List<Object>> rows,
                                                 List<Object> header) throws IOException {
        if (rows.isEmpty()) {
            return rows;
        }
        if (!isSheetEmpty(sheetName)) {
            return rows;
        }
        List<List<Object>> payload = new ArrayList<>(rows.size() + 1);
        payload.add(header);
        payload.addAll(rows);
        return payload;
    }

    private boolean isSheetEmpty(String sheetName) throws IOException {
        String range = "'" + sheetName + "'!A1:A1";
        ValueRange response = sheets.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        if (response.getValues() == null || response.getValues().isEmpty()) {
            return true;
        }
        return response.getValues().get(0).isEmpty();
    }

}
