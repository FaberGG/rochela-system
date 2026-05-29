CREATE OR REPLACE VIEW v_produccion_etapas_completas AS
SELECT
    -- Lote
    l.id                            AS lote_id,
    l.codigo_lote                   AS codigo_lote,
    pr.codigo                       AS codigo_producto,
    pr.nombre                       AS nombre_producto,
    l.fecha_vencimiento             AS fecha_vencimiento,
    DATE(l.fecha_hora_inicio)       AS fecha_produccion,
    TO_CHAR(l.fecha_hora_inicio, 'HH24:MI') AS hora_inicio,
    l.estado_actual                 AS estado_actual,
 
    -- Lote de leche de origen
    ll.codigo_lote_leche            AS codigo_lote_leche,
    ll.cantidad_litros_total        AS litros_totales_mezcla,
 
    -- Etapa base
    er.id                           AS etapa_id,
    er.tipo_etapa                   AS tipo_etapa,
    er.hora                         AS hora_etapa,
    er.fecha_hora_registro          AS timestamp_registro_etapa,
 
    -- Pasteurización
    ep.temperatura                  AS pasteurizacion_temperatura_c,
 
    -- Cloruro
    ec.temperatura                  AS cloruro_temperatura_c,
    ec.cantidad_gramos              AS cloruro_gramos,
    ec.lote_cloruro                 AS cloruro_lote_insumo,
 
    -- Cuajo
    ecj.temperatura                 AS cuajo_temperatura_c,
    ecj.cantidad_gramos             AS cuajo_gramos,
    ecj.lote_cuajo                  AS cuajo_lote_insumo,
 
    -- Lavado/Desuerado
    eld.litros                      AS lavado_desuerado_litros,
 
    -- Desuerado
    ed.litros                       AS desuerado_litros,
 
    -- Salado
    es.temperatura                  AS salado_temperatura_c,
    es.cantidad_kg                  AS salado_kg,
    es.sodio_inicial                AS sodio_inicial_pct,
    es.sodio_final                  AS sodio_final_pct,
    (es.sodio_final - es.sodio_inicial)
                                    AS diferencia_sodio_pct,
    es.lote_sal                     AS sal_lote_insumo,
 
    -- Prensado
    epr.hora_fin                    AS prensado_hora_fin,
    epr.duracion_minutos            AS prensado_duracion_minutos,
    ROUND(epr.duracion_minutos::NUMERIC / 60, 2)
                                    AS prensado_duracion_horas,
    epr.presion_psi                 AS prensado_presion_psi,
    epr.responsable                 AS prensado_responsable,
 
    -- Cierre
    cl.unidades_producidas          AS unidades_producidas,
    cl.peso_total_kg                AS peso_total_kg,
    ROUND(cl.rendimiento_general::NUMERIC, 4)
                                    AS rendimiento_general_pct,
    ROUND(cl.rendimiento_teorico::NUMERIC, 4)
                                    AS rendimiento_teorico_pct,
    cl.fecha_hora_cierre            AS fecha_hora_cierre,
    l.observaciones                 AS observaciones_lote
 
FROM lote l
INNER JOIN producto pr          ON pr.id  = l.producto_id
INNER JOIN lote_leche ll        ON ll.id  = l.lote_leche_id
INNER JOIN etapa_registro er    ON er.lote_id = l.id
LEFT  JOIN etapa_pasteurizacion ep  ON ep.id  = er.id AND er.tipo_etapa = 'PASTEURIZACION'
LEFT  JOIN etapa_cloruro ec         ON ec.id  = er.id AND er.tipo_etapa = 'CLORURO'
LEFT  JOIN etapa_cuajo ecj          ON ecj.id = er.id AND er.tipo_etapa = 'CUAJO'
LEFT  JOIN etapa_lavado_desuerado eld ON eld.id = er.id AND er.tipo_etapa = 'LAVADO_DESUERADO'
LEFT  JOIN etapa_desuerado ed       ON ed.id  = er.id AND er.tipo_etapa = 'DESUERADO'
LEFT  JOIN etapa_salado es          ON es.id  = er.id AND er.tipo_etapa = 'SALADO'
LEFT  JOIN etapa_prensado epr       ON epr.id = er.id AND er.tipo_etapa = 'PRENSADO'
LEFT  JOIN cierre_lote cl           ON cl.lote_id = l.id
ORDER BY l.fecha_hora_inicio DESC, er.hora ASC;