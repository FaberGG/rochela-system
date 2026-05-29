CREATE OR REPLACE VIEW v_calidad_por_proveedor AS
SELECT
    -- Identificación del proveedor
    p.id                                        AS proveedor_id,
    p.nombre_empresa                            AS proveedor,
 
    -- Volumen
    COUNT(rl.id)                                AS total_recepciones,
    SUM(rl.cantidad_litros)                     AS total_litros_recibidos,
    ROUND(AVG(rl.cantidad_litros)::NUMERIC, 2)  AS promedio_litros_por_recepcion,
    MIN(rl.fecha)                               AS primera_recepcion,
    MAX(rl.fecha)                               AS ultima_recepcion,
 
    -- Resultados de calidad
    COUNT(*) FILTER (WHERE rl.resultado_validacion = 'APTA')
                                                AS recepciones_aptas,
    COUNT(*) FILTER (WHERE rl.resultado_validacion = 'CONDICIONAL')
                                                AS recepciones_condicionales,
    COUNT(*) FILTER (WHERE rl.resultado_validacion = 'NO_APTA')
                                                AS recepciones_no_aptas,
    ROUND(
        100.0 * COUNT(*) FILTER (WHERE rl.resultado_validacion = 'APTA')
        / NULLIF(COUNT(*), 0), 1
    )                                           AS pct_apta,
    ROUND(
        100.0 * COUNT(*) FILTER (WHERE rl.resultado_validacion = 'NO_APTA')
        / NULLIF(COUNT(*), 0), 1
    )                                           AS pct_no_apta,
 
    -- Promedios de parámetros fisicoquímicos
    ROUND(AVG(rl.ph)::NUMERIC, 3)               AS ph_promedio,
    ROUND(AVG(rl.densidad)::NUMERIC, 4)         AS densidad_promedio,
    ROUND(AVG(rl.proteina)::NUMERIC, 3)         AS proteina_promedio_pct,
    ROUND(AVG(rl.grasa)::NUMERIC, 3)            AS grasa_promedio_pct,
    ROUND(AVG(rl.solidos_totales)::NUMERIC, 3)  AS solidos_totales_promedio_pct,
    ROUND(AVG(rl.acidez_titulable)::NUMERIC, 4) AS acidez_promedio,
    ROUND(AVG(rl.agua_anadida)::NUMERIC, 3)     AS agua_anadida_promedio_pct,
    ROUND(AVG(rl.punto_crioscopico)::NUMERIC, 3) AS crioscopico_promedio,
    ROUND(AVG(rl.temperatura)::NUMERIC, 2)      AS temperatura_promedio_c,
 
    -- Valores mínimos y máximos históricos
    ROUND(MIN(rl.ph)::NUMERIC, 3)               AS ph_minimo,
    ROUND(MAX(rl.ph)::NUMERIC, 3)               AS ph_maximo,
    ROUND(MIN(rl.grasa)::NUMERIC, 3)            AS grasa_minima_pct,
    ROUND(MAX(rl.grasa)::NUMERIC, 3)            AS grasa_maxima_pct,
    ROUND(MIN(rl.proteina)::NUMERIC, 3)         AS proteina_minima_pct,
    ROUND(MAX(rl.proteina)::NUMERIC, 3)         AS proteina_maxima_pct,
 
    -- Frecuencia de incumplimiento por parámetro
    COUNT(*) FILTER (WHERE rl.ph NOT BETWEEN 6.6 AND 6.8)
                                                AS veces_ph_fuera_rango,
    COUNT(*) FILTER (WHERE rl.densidad NOT BETWEEN 1.028 AND 1.033)
                                                AS veces_densidad_fuera_rango,
    COUNT(*) FILTER (WHERE rl.grasa < 4.0)
                                                AS veces_grasa_baja,
    COUNT(*) FILTER (WHERE rl.proteina < 3.50)
                                                AS veces_proteina_baja,
    COUNT(*) FILTER (WHERE rl.agua_anadida >= 1.0)
                                                AS veces_agua_alta,
    COUNT(*) FILTER (WHERE rl.punto_crioscopico NOT BETWEEN 0.53 AND 0.55)
                                                AS veces_crioscopico_fuera,
    COUNT(*) FILTER (WHERE rl.alcohol = 'POSITIVO')
                                                AS veces_alcohol_positivo,
 
    -- Reductasa
    ROUND(AVG(rl.tiempo_reductasa_minutos)::NUMERIC, 1)
                                                AS reductasa_minutos_promedio,
    COUNT(*) FILTER (WHERE rl.tiempo_reductasa_minutos < 180)
                                                AS veces_reductasa_no_cumple,
    COUNT(*) FILTER (WHERE rl.estado_recepcion = 'PENDIENTE_REDUCTASA')
                                                AS reductasas_pendientes_actuales
 
FROM recepcion_leche rl
INNER JOIN proveedor p ON p.id = rl.proveedor_id
GROUP BY p.id, p.nombre_empresa
ORDER BY total_litros_recibidos DESC;