CREATE OR REPLACE VIEW v_trazabilidad_lote_completa AS
SELECT
    -- Lote de queso
    l.id                            AS lote_id,
    l.codigo_lote                   AS codigo_lote_queso,
    pr.codigo                       AS codigo_producto,
    pr.nombre                       AS nombre_producto,
    l.fecha_hora_inicio             AS fecha_inicio_produccion,
    DATE(l.fecha_hora_inicio)       AS fecha_produccion,
    l.fecha_vencimiento             AS fecha_vencimiento,
    l.estado_actual                 AS estado_lote,
 
    -- Cierre del lote (si existe)
    cl.unidades_producidas          AS unidades_producidas,
    cl.peso_total_kg                AS peso_total_kg,
    cl.rendimiento_general          AS rendimiento_general_pct,
    cl.rendimiento_teorico          AS rendimiento_teorico_pct,
    cl.fecha_hora_cierre            AS fecha_hora_cierre,
    l.observaciones                 AS observaciones_lote,
 
    -- Lote de leche
    ll.id                           AS lote_leche_id,
    ll.codigo_lote_leche            AS codigo_lote_leche,
    ll.cantidad_litros_total        AS litros_totales_en_mezcla,
    ll.tanque_proceso               AS tanque_proceso,
 
    -- Parámetros fisicoquímicos de la mezcla (análisis del lote de leche)
    ll.ph                           AS mezcla_ph,
    ll.grasa                        AS mezcla_grasa_pct,
    ll.proteina                     AS mezcla_proteina_pct,
    ll.densidad                     AS mezcla_densidad,
    ll.temperatura                  AS mezcla_temperatura_c,
    ll.solidos_totales              AS mezcla_solidos_totales_pct,
    ll.solidos_no_grasos            AS mezcla_solidos_no_grasos_pct,
    ll.punto_crioscopico            AS mezcla_punto_crioscopico,
    ll.lactosa                      AS mezcla_lactosa_pct,
    ll.agua_anadida                 AS mezcla_agua_anadida_pct,
    ll.observaciones                AS observaciones_mezcla,
 
    -- Recepción individual que aportó al lote de leche
    rl.id                           AS recepcion_id,
    rl.fecha                        AS fecha_recepcion,
    rl.jornada                      AS jornada_recepcion,
    rl.cantidad_litros              AS litros_aportados_de_esta_recepcion,
    ROUND(
    (100.0 * rl.cantidad_litros / NULLIF(ll.cantidad_litros_total, 0))::numeric, 1
    )                              AS pct_aporte_al_lote_leche,
 
    -- Proveedor de esa recepción
    p.id                            AS proveedor_id,
    p.nombre_empresa                AS proveedor,
    p.direccion                     AS direccion_proveedor,
 
    -- Calidad de la recepción individual
    rl.resultado_validacion         AS calidad_recepcion,
    rl.analisis_organoleptico       AS organoleptico,
    rl.alcohol                      AS alcohol,
    rl.temperatura                  AS recepcion_temperatura_c,
    rl.densidad                     AS recepcion_densidad,
    rl.ph                           AS recepcion_ph,
    rl.proteina                     AS recepcion_proteina_pct,
    rl.grasa                        AS recepcion_grasa_pct,
    rl.solidos_totales              AS recepcion_solidos_totales_pct,
    rl.acidez_titulable             AS recepcion_acidez,
    rl.agua_anadida                 AS recepcion_agua_anadida_pct,
    rl.punto_crioscopico            AS recepcion_punto_crioscopico,
    rl.tiempo_reductasa_minutos     AS recepcion_reductasa_minutos,
    (rl.tiempo_reductasa_minutos >= 180)
                                    AS recepcion_reductasa_cumple,
    rl.observaciones                AS observaciones_recepcion
 
FROM lote l
INNER JOIN producto pr       ON pr.id  = l.producto_id
INNER JOIN lote_leche ll     ON ll.id  = l.lote_leche_id
INNER JOIN lote_leche_recepcion llr ON llr.lote_leche_id = ll.id
INNER JOIN recepcion_leche rl ON rl.id = llr.recepcion_leche_id
INNER JOIN proveedor p       ON p.id   = rl.proveedor_id
LEFT  JOIN cierre_lote cl    ON cl.lote_id = l.id
ORDER BY l.fecha_hora_inicio DESC, rl.fecha DESC;