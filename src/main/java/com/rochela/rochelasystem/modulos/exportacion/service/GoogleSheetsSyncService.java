package com.rochela.rochelasystem.modulos.exportacion.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.rochela.rochelasystem.modulos.exportacion.config.GoogleSheetsClientProvider;
import com.rochela.rochelasystem.modulos.exportacion.model.VistaEtapasProcesoEntity;
import com.rochela.rochelasystem.modulos.exportacion.model.VistaRendimientoEntity;
import com.rochela.rochelasystem.modulos.exportacion.repository.VistaEtapasProcesoRepository;
import com.rochela.rochelasystem.modulos.exportacion.repository.VistaRendimientoRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
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
            "recepcion_id",
            "fecha",
            "hora_registro",
            "jornada",
            "proveedor_id",
            "proveedor",
            "tanque_destino",
            "litros_recibidos",
            "registrado_por",
            "resultado_organoleptico",
            "color",
            "olor",
            "prueba_alcohol",
            "temperatura_c",
            "densidad_g_ml",
            "ph",
            "proteina_pct",
            "grasa_pct",
            "solidos_totales_pct",
            "acidez_titulable",
            "agua_anadida_pct",
            "punto_crioscopico_c",
            "ph_min",
            "ph_max",
            "densidad_min",
            "densidad_max",
            "proteina_min",
            "grasa_min",
            "solidos_totales_min",
            "acidez_min",
            "acidez_max",
            "agua_max",
            "crioscopico_min",
            "crioscopico_max",
            "ph_ok",
            "densidad_ok",
            "proteina_ok",
            "grasa_ok",
            "solidos_totales_ok",
            "acidez_ok",
            "agua_ok",
            "crioscopico_ok",
            "parametros_fuera_rango",
            "reductasa_hora_inicio",
            "reductasa_hora_fin",
            "reductasa_minutos",
            "reductasa_horas",
            "reductasa_cumple",
            "resultado_validacion",
            "estado_recepcion",
            "observaciones",
            "timestamp_registro"
    );

    private static final int RECEPCIONES_COLUMN_COUNT = HEADERS_RECEPCIONES.size();
    private static final String SQL_RECEPCIONES_COMPLETAS = """
            SELECT
                recepcion_id,
                fecha,
                hora_registro,
                jornada,
                proveedor_id,
                proveedor,
                tanque_destino,
                litros_recibidos,
                registrado_por,
                resultado_organoleptico,
                color,
                olor,
                prueba_alcohol,
                temperatura_c,
                densidad_g_ml,
                ph,
                proteina_pct,
                grasa_pct,
                solidos_totales_pct,
                acidez_titulable,
                agua_anadida_pct,
                punto_crioscopico_c,
                ph_min,
                ph_max,
                densidad_min,
                densidad_max,
                proteina_min,
                grasa_min,
                solidos_totales_min,
                acidez_min,
                acidez_max,
                agua_max,
                crioscopico_min,
                crioscopico_max,
                ph_ok,
                densidad_ok,
                proteina_ok,
                grasa_ok,
                solidos_totales_ok,
                acidez_ok,
                agua_ok,
                crioscopico_ok,
                parametros_fuera_rango,
                reductasa_hora_inicio,
                reductasa_hora_fin,
                reductasa_minutos,
                reductasa_horas,
                reductasa_cumple,
                resultado_validacion,
                estado_recepcion,
                observaciones,
                timestamp_registro
            FROM v_recepciones_completas
            ORDER BY fecha DESC, timestamp_registro DESC
            """;

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

    private static final List<Object> HEADERS_CALIDAD_PROVEEDOR = List.of(
            "proveedor_id",
            "proveedor",
            "total_recepciones",
            "total_litros_recibidos",
            "promedio_litros_por_recepcion",
            "primera_recepcion",
            "ultima_recepcion",
            "recepciones_aptas",
            "recepciones_condicionales",
            "recepciones_no_aptas",
            "pct_apta",
            "pct_no_apta",
            "ph_promedio",
            "densidad_promedio",
            "proteina_promedio_pct",
            "grasa_promedio_pct",
            "solidos_totales_promedio_pct",
            "acidez_promedio",
            "agua_anadida_promedio_pct",
            "crioscopico_promedio",
            "temperatura_promedio_c",
            "ph_minimo",
            "ph_maximo",
            "grasa_minima_pct",
            "grasa_maxima_pct",
            "proteina_minima_pct",
            "proteina_maxima_pct",
            "veces_ph_fuera_rango",
            "veces_densidad_fuera_rango",
            "veces_grasa_baja",
            "veces_proteina_baja",
            "veces_agua_alta",
            "veces_crioscopico_fuera",
            "veces_alcohol_positivo",
            "reductasa_minutos_promedio",
            "veces_reductasa_no_cumple",
            "reductasas_pendientes_actuales"
    );

    private static final int CALIDAD_PROVEEDOR_COLUMN_COUNT = HEADERS_CALIDAD_PROVEEDOR.size();
    private static final String SQL_CALIDAD_POR_PROVEEDOR = """
            SELECT
                proveedor_id,
                proveedor,
                total_recepciones,
                total_litros_recibidos,
                promedio_litros_por_recepcion,
                primera_recepcion,
                ultima_recepcion,
                recepciones_aptas,
                recepciones_condicionales,
                recepciones_no_aptas,
                pct_apta,
                pct_no_apta,
                ph_promedio,
                densidad_promedio,
                proteina_promedio_pct,
                grasa_promedio_pct,
                solidos_totales_promedio_pct,
                acidez_promedio,
                agua_anadida_promedio_pct,
                crioscopico_promedio,
                temperatura_promedio_c,
                ph_minimo,
                ph_maximo,
                grasa_minima_pct,
                grasa_maxima_pct,
                proteina_minima_pct,
                proteina_maxima_pct,
                veces_ph_fuera_rango,
                veces_densidad_fuera_rango,
                veces_grasa_baja,
                veces_proteina_baja,
                veces_agua_alta,
                veces_crioscopico_fuera,
                veces_alcohol_positivo,
                reductasa_minutos_promedio,
                veces_reductasa_no_cumple,
                reductasas_pendientes_actuales
            FROM v_calidad_por_proveedor
            ORDER BY total_litros_recibidos DESC
            """;

    private static final List<Object> HEADERS_TRAZABILIDAD_LOTES = List.of(
            "lote_id",
            "codigo_lote_queso",
            "codigo_producto",
            "nombre_producto",
            "fecha_inicio_produccion",
            "fecha_produccion",
            "fecha_vencimiento",
            "estado_lote",
            "unidades_producidas",
            "peso_total_kg",
            "rendimiento_general_pct",
            "rendimiento_teorico_pct",
            "fecha_hora_cierre",
            "observaciones_lote",
            "lote_leche_id",
            "codigo_lote_leche",
            "litros_totales_en_mezcla",
            "tanque_proceso",
            "mezcla_ph",
            "mezcla_grasa_pct",
            "mezcla_proteina_pct",
            "mezcla_densidad",
            "mezcla_temperatura_c",
            "mezcla_solidos_totales_pct",
            "mezcla_solidos_no_grasos_pct",
            "mezcla_punto_crioscopico",
            "mezcla_lactosa_pct",
            "mezcla_agua_anadida_pct",
            "observaciones_mezcla",
            "recepcion_id",
            "fecha_recepcion",
            "jornada_recepcion",
            "litros_aportados_de_esta_recepcion",
            "pct_aporte_al_lote_leche",
            "proveedor_id",
            "proveedor",
            "direccion_proveedor",
            "calidad_recepcion",
            "organoleptico",
            "alcohol",
            "recepcion_temperatura_c",
            "recepcion_densidad",
            "recepcion_ph",
            "recepcion_proteina_pct",
            "recepcion_grasa_pct",
            "recepcion_solidos_totales_pct",
            "recepcion_acidez",
            "recepcion_agua_anadida_pct",
            "recepcion_punto_crioscopico",
            "recepcion_reductasa_minutos",
            "recepcion_reductasa_cumple",
            "observaciones_recepcion"
    );

    private static final int TRAZABILIDAD_LOTES_COLUMN_COUNT = HEADERS_TRAZABILIDAD_LOTES.size();
    private static final String SQL_TRAZABILIDAD_LOTE_COMPLETA = """
            SELECT
                lote_id,
                codigo_lote_queso,
                codigo_producto,
                nombre_producto,
                fecha_inicio_produccion,
                fecha_produccion,
                fecha_vencimiento,
                estado_lote,
                unidades_producidas,
                peso_total_kg,
                rendimiento_general_pct,
                rendimiento_teorico_pct,
                fecha_hora_cierre,
                observaciones_lote,
                lote_leche_id,
                codigo_lote_leche,
                litros_totales_en_mezcla,
                tanque_proceso,
                mezcla_ph,
                mezcla_grasa_pct,
                mezcla_proteina_pct,
                mezcla_densidad,
                mezcla_temperatura_c,
                mezcla_solidos_totales_pct,
                mezcla_solidos_no_grasos_pct,
                mezcla_punto_crioscopico,
                mezcla_lactosa_pct,
                mezcla_agua_anadida_pct,
                observaciones_mezcla,
                recepcion_id,
                fecha_recepcion,
                jornada_recepcion,
                litros_aportados_de_esta_recepcion,
                pct_aporte_al_lote_leche,
                proveedor_id,
                proveedor,
                direccion_proveedor,
                calidad_recepcion,
                organoleptico,
                alcohol,
                recepcion_temperatura_c,
                recepcion_densidad,
                recepcion_ph,
                recepcion_proteina_pct,
                recepcion_grasa_pct,
                recepcion_solidos_totales_pct,
                recepcion_acidez,
                recepcion_agua_anadida_pct,
                recepcion_punto_crioscopico,
                recepcion_reductasa_minutos,
                recepcion_reductasa_cumple,
                observaciones_recepcion
            FROM v_trazabilidad_lote_completa
            ORDER BY fecha_inicio_produccion DESC, fecha_recepcion DESC
            """;

    private static final List<Object> HEADERS_PRODUCCION_ETAPAS_COMPLETAS = List.of(
            "lote_id",
            "codigo_lote",
            "codigo_producto",
            "nombre_producto",
            "fecha_vencimiento",
            "fecha_produccion",
            "hora_inicio",
            "estado_actual",
            "codigo_lote_leche",
            "litros_totales_mezcla",
            "etapa_id",
            "tipo_etapa",
            "hora_etapa",
            "timestamp_registro_etapa",
            "pasteurizacion_temperatura_c",
            "cloruro_temperatura_c",
            "cloruro_gramos",
            "cloruro_lote_insumo",
            "cuajo_temperatura_c",
            "cuajo_gramos",
            "cuajo_lote_insumo",
            "lavado_desuerado_litros",
            "desuerado_litros",
            "salado_temperatura_c",
            "salado_kg",
            "sodio_inicial_pct",
            "sodio_final_pct",
            "diferencia_sodio_pct",
            "sal_lote_insumo",
            "prensado_hora_fin",
            "prensado_duracion_minutos",
            "prensado_duracion_horas",
            "prensado_presion_psi",
            "prensado_responsable",
            "unidades_producidas",
            "peso_total_kg",
            "rendimiento_general_pct",
            "rendimiento_teorico_pct",
            "fecha_hora_cierre",
            "observaciones_lote"
    );

    private static final int PRODUCCION_ETAPAS_COMPLETAS_COLUMN_COUNT = HEADERS_PRODUCCION_ETAPAS_COMPLETAS.size();
    private static final String SQL_PRODUCCION_ETAPAS_COMPLETAS = """
            SELECT
                lote_id,
                codigo_lote,
                codigo_producto,
                nombre_producto,
                fecha_vencimiento,
                fecha_produccion,
                hora_inicio,
                estado_actual,
                codigo_lote_leche,
                litros_totales_mezcla,
                etapa_id,
                tipo_etapa,
                hora_etapa,
                timestamp_registro_etapa,
                pasteurizacion_temperatura_c,
                cloruro_temperatura_c,
                cloruro_gramos,
                cloruro_lote_insumo,
                cuajo_temperatura_c,
                cuajo_gramos,
                cuajo_lote_insumo,
                lavado_desuerado_litros,
                desuerado_litros,
                salado_temperatura_c,
                salado_kg,
                sodio_inicial_pct,
                sodio_final_pct,
                diferencia_sodio_pct,
                sal_lote_insumo,
                prensado_hora_fin,
                prensado_duracion_minutos,
                prensado_duracion_horas,
                prensado_presion_psi,
                prensado_responsable,
                unidades_producidas,
                peso_total_kg,
                rendimiento_general_pct,
                rendimiento_teorico_pct,
                fecha_hora_cierre,
                observaciones_lote
            FROM v_produccion_etapas_completas
            ORDER BY fecha_produccion DESC, hora_etapa ASC
            """;

    private static final List<Object> HEADERS_CORTES_POR_LOTE = List.of(
            "lote_id",
            "codigo_lote",
            "codigo_producto",
            "nombre_producto",
            "fecha_produccion",
            "estado_lote",
            "corte_id",
            "numero_corte",
            "hora_corte",
            "observacion_corte",
            "timestamp_registro",
            "total_cortes_en_lote"
    );

    private static final int CORTES_POR_LOTE_COLUMN_COUNT = HEADERS_CORTES_POR_LOTE.size();
    private static final String SQL_CORTES_POR_LOTE = """
            SELECT
                lote_id,
                codigo_lote,
                codigo_producto,
                nombre_producto,
                fecha_produccion,
                estado_lote,
                corte_id,
                numero_corte,
                hora_corte,
                observacion_corte,
                timestamp_registro,
                total_cortes_en_lote
            FROM v_cortes_por_lote
            ORDER BY fecha_produccion DESC, numero_corte ASC
            """;

    private final GoogleSheetsClientProvider sheetsProvider;
    private final VistaRendimientoRepository vistaRendimientoRepository;
    private final VistaEtapasProcesoRepository vistaEtapasProcesoRepository;
    private final JdbcTemplate jdbcTemplate;

    @Value("${exportacion.google.spreadsheet-id}")
    private String spreadsheetId;

    @Value("${exportacion.google.sheets.rendimiento}")
    private String sheetRendimiento;

    @Value("${exportacion.google.sheets.recepciones}")
    private String sheetRecepciones;

    @Value("${exportacion.google.sheets.etapas}")
    private String sheetEtapas;

    @Value("${exportacion.google.sheets.calidad-por-proveedor}")
    private String sheetCalidadPorProveedor;

    @Value("${exportacion.google.sheets.trazabilidad-lotes}")
    private String sheetTrazabilidadLotes;

    @Value("${exportacion.google.sheets.produccion-etapas-completas}")
    private String sheetProduccionEtapasCompletas;

    @Value("${exportacion.google.sheets.cortes-por-lote}")
    private String sheetCortesPorLote;

    public GoogleSheetsSyncService(GoogleSheetsClientProvider sheetsProvider,
                                   VistaRendimientoRepository vistaRendimientoRepository,
                                   VistaEtapasProcesoRepository vistaEtapasProcesoRepository,
                                   JdbcTemplate jdbcTemplate) {
        this.sheetsProvider = sheetsProvider;
        this.vistaRendimientoRepository = vistaRendimientoRepository;
        this.vistaEtapasProcesoRepository = vistaEtapasProcesoRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Async
    @Scheduled(cron = "0 0 12,18 * * *")
    public void sincronizar() throws IOException {
        Optional<Sheets> sheets = sheetsProvider.getSheets();
        if (sheets.isEmpty()) {
            return;
        }
        sincronizarRecepciones(sheets.get());
        sincronizarCalidadPorProveedor(sheets.get());
        sincronizarTrazabilidadLotes(sheets.get());
        sincronizarProduccionEtapasCompletas(sheets.get());
        sincronizarCortesPorLote(sheets.get());
    }

    private void sincronizarRendimiento(Sheets sheets) throws IOException {
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

        List<List<Object>> payload = withHeaderIfEmpty(sheets, sheetRendimiento, rows, HEADERS_RENDIMIENTO);
        appendRows(sheets, sheetRendimiento, payload);
        pendientes.forEach(item -> item.setSincronizadoSheets(true));
        vistaRendimientoRepository.saveAll(pendientes);
    }

    private void sincronizarRecepciones(Sheets sheets) throws IOException {
        List<List<Object>> rows = jdbcTemplate.query(SQL_RECEPCIONES_COMPLETAS, (rs, rowNum) -> {
            List<Object> row = new ArrayList<>(RECEPCIONES_COLUMN_COUNT);
            for (int i = 1; i <= RECEPCIONES_COLUMN_COUNT; i++) {
                row.add(normalizeCellValue(rs.getObject(i)));
            }
            return row;
        });

        List<List<Object>> payload = new ArrayList<>(rows.size() + 1);
        payload.add(HEADERS_RECEPCIONES);
        payload.addAll(rows);
        replaceSheetRows(sheets, sheetRecepciones, payload);
    }

    private void sincronizarEtapas(Sheets sheets) throws IOException {
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

        List<List<Object>> payload = withHeaderIfEmpty(sheets, sheetEtapas, rows, HEADERS_ETAPAS);
        appendRows(sheets, sheetEtapas, payload);
        pendientes.forEach(item -> item.setSincronizadoSheets(true));
        vistaEtapasProcesoRepository.saveAll(pendientes);
    }

    private void sincronizarCalidadPorProveedor(Sheets sheets) throws IOException {
        List<List<Object>> rows = jdbcTemplate.query(SQL_CALIDAD_POR_PROVEEDOR, (rs, rowNum) -> {
            List<Object> row = new ArrayList<>(CALIDAD_PROVEEDOR_COLUMN_COUNT);
            for (int i = 1; i <= CALIDAD_PROVEEDOR_COLUMN_COUNT; i++) {
                row.add(normalizeCellValue(rs.getObject(i)));
            }
            return row;
        });

        List<List<Object>> payload = new ArrayList<>(rows.size() + 1);
        payload.add(HEADERS_CALIDAD_PROVEEDOR);
        payload.addAll(rows);
        replaceSheetRows(sheets, sheetCalidadPorProveedor, payload);
    }

    private void sincronizarTrazabilidadLotes(Sheets sheets) throws IOException {
        List<List<Object>> rows = jdbcTemplate.query(SQL_TRAZABILIDAD_LOTE_COMPLETA, (rs, rowNum) -> {
            List<Object> row = new ArrayList<>(TRAZABILIDAD_LOTES_COLUMN_COUNT);
            for (int i = 1; i <= TRAZABILIDAD_LOTES_COLUMN_COUNT; i++) {
                row.add(normalizeCellValue(rs.getObject(i)));
            }
            return row;
        });

        List<List<Object>> payload = new ArrayList<>(rows.size() + 1);
        payload.add(HEADERS_TRAZABILIDAD_LOTES);
        payload.addAll(rows);
        replaceSheetRows(sheets, sheetTrazabilidadLotes, payload);
    }

    private void sincronizarProduccionEtapasCompletas(Sheets sheets) throws IOException {
        List<List<Object>> rows = jdbcTemplate.query(SQL_PRODUCCION_ETAPAS_COMPLETAS, (rs, rowNum) -> {
            List<Object> row = new ArrayList<>(PRODUCCION_ETAPAS_COMPLETAS_COLUMN_COUNT);
            for (int i = 1; i <= PRODUCCION_ETAPAS_COMPLETAS_COLUMN_COUNT; i++) {
                row.add(normalizeCellValue(rs.getObject(i)));
            }
            return row;
        });

        List<List<Object>> payload = new ArrayList<>(rows.size() + 1);
        payload.add(HEADERS_PRODUCCION_ETAPAS_COMPLETAS);
        payload.addAll(rows);
        replaceSheetRows(sheets, sheetProduccionEtapasCompletas, payload);
    }

    private void sincronizarCortesPorLote(Sheets sheets) throws IOException {
        List<List<Object>> rows = jdbcTemplate.query(SQL_CORTES_POR_LOTE, (rs, rowNum) -> {
            List<Object> row = new ArrayList<>(CORTES_POR_LOTE_COLUMN_COUNT);
            for (int i = 1; i <= CORTES_POR_LOTE_COLUMN_COUNT; i++) {
                row.add(normalizeCellValue(rs.getObject(i)));
            }
            return row;
        });

        List<List<Object>> payload = new ArrayList<>(rows.size() + 1);
        payload.add(HEADERS_CORTES_POR_LOTE);
        payload.addAll(rows);
        replaceSheetRows(sheets, sheetCortesPorLote, payload);
    }

    private void appendRows(Sheets sheets,
                            String sheetName,
                            List<List<Object>> rows) throws IOException {
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

    private void replaceSheetRows(Sheets sheets,
                                  String sheetName,
                                  List<List<Object>> rows) throws IOException {
        String clearRange = "'" + sheetName + "'!A:ZZ";
        sheets.spreadsheets().values()
                .clear(spreadsheetId, clearRange, new ClearValuesRequest())
                .execute();

        String writeRange = "'" + sheetName + "'!A1";
        ValueRange body = new ValueRange().setValues(rows);
        sheets.spreadsheets().values()
                .update(spreadsheetId, writeRange, body)
                .setValueInputOption("RAW")
                .execute();
    }

    private Object normalizeCellValue(Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof Number || value instanceof Boolean || value instanceof String) {
            return value;
        }
        return value.toString();
    }

    private String toText(Object value) {
        return value != null ? value.toString() : null;
    }

    private List<Object> rowOf(Object... values) {
        return Arrays.asList(values);
    }

    private List<List<Object>> withHeaderIfEmpty(Sheets sheets,
                                                 String sheetName,
                                                 List<List<Object>> rows,
                                                 List<Object> header) throws IOException {
        if (rows.isEmpty()) {
            return rows;
        }
        if (!isSheetEmpty(sheets, sheetName)) {
            return rows;
        }
        List<List<Object>> payload = new ArrayList<>(rows.size() + 1);
        payload.add(header);
        payload.addAll(rows);
        return payload;
    }

    private boolean isSheetEmpty(Sheets sheets,
                                 String sheetName) throws IOException {
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
