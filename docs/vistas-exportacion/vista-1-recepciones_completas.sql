CREATE OR REPLACE VIEW v_recepciones_completas AS
SELECT
    -- Identificación
    rl.id                           AS recepcion_id,
    rl.fecha                        AS fecha,
    TO_CHAR(rl.fecha_hora, 'HH24:MI') AS hora_registro,
    rl.jornada                      AS jornada,

    -- Proveedor
    p.id                            AS proveedor_id,
    p.nombre_empresa                AS proveedor,

    -- Datos de recepción
    rl.ubicacion                    AS tanque_destino,
    rl.cantidad_litros              AS litros_recibidos,
    rl.realizado_por                AS registrado_por,

    -- Análisis organoléptico
    rl.analisis_organoleptico       AS resultado_organoleptico,
    CASE WHEN rl.color_cumple THEN 'Normal' ELSE 'Anormal' END AS color,
    CASE WHEN rl.olor_cumple  THEN 'Normal' ELSE 'Anormal' END AS olor,
    rl.alcohol                      AS prueba_alcohol,

    -- Parámetros fisicoquímicos
    rl.temperatura                  AS temperatura_c,
    rl.densidad                     AS densidad_g_ml,
    rl.ph                           AS ph,
    rl.proteina                     AS proteina_pct,
    rl.grasa                        AS grasa_pct,
    rl.solidos_totales              AS solidos_totales_pct,
    rl.acidez_titulable             AS acidez_titulable,
    rl.agua_anadida                 AS agua_anadida_pct,
    rl.punto_crioscopico            AS punto_crioscopico_c,

    -- Rangos de referencia (para facilitar análisis en Sheets)
    6.6  AS ph_min,        6.8  AS ph_max,
    1.028 AS densidad_min, 1.033 AS densidad_max,
    3.50  AS proteina_min,
    4.0   AS grasa_min,
    13.0  AS solidos_totales_min,
    0.16  AS acidez_min,   0.18  AS acidez_max,
    1.0   AS agua_max,
    0.53  AS crioscopico_min, 0.55 AS crioscopico_max,

    -- Indicadores de cumplimiento por parámetro (TRUE = dentro del rango)
    (rl.ph BETWEEN 6.6 AND 6.8)                             AS ph_ok,
    (rl.densidad BETWEEN 1.028 AND 1.033)                   AS densidad_ok,
    (rl.proteina >= 3.50)                                   AS proteina_ok,
    (rl.grasa >= 4.0)                                       AS grasa_ok,
    (rl.solidos_totales >= 13.0)                            AS solidos_totales_ok,
    (rl.acidez_titulable BETWEEN 0.16 AND 0.18)             AS acidez_ok,
    (rl.agua_anadida < 1.0)                                 AS agua_ok,
    (rl.punto_crioscopico BETWEEN 0.53 AND 0.55)            AS crioscopico_ok,

    -- Conteo de parámetros fuera de rango
    (
        CASE WHEN rl.ph NOT BETWEEN 6.6 AND 6.8                 THEN 1 ELSE 0 END +
        CASE WHEN rl.densidad NOT BETWEEN 1.028 AND 1.033       THEN 1 ELSE 0 END +
        CASE WHEN rl.proteina < 3.50                            THEN 1 ELSE 0 END +
        CASE WHEN rl.grasa < 4.0                                THEN 1 ELSE 0 END +
        CASE WHEN rl.solidos_totales < 13.0                     THEN 1 ELSE 0 END +
        CASE WHEN rl.acidez_titulable NOT BETWEEN 0.16 AND 0.18 THEN 1 ELSE 0 END +
        CASE WHEN rl.agua_anadida >= 1.0                        THEN 1 ELSE 0 END +
        CASE WHEN rl.punto_crioscopico NOT BETWEEN 0.53 AND 0.55 THEN 1 ELSE 0 END
    )                                                           AS parametros_fuera_rango,

    -- Reductasa
    rl.hora_inicio_reductasa        AS reductasa_hora_inicio,
    rl.hora_fin_reductasa           AS reductasa_hora_fin,
    rl.tiempo_reductasa_minutos     AS reductasa_minutos,
    ROUND(rl.tiempo_reductasa_minutos::NUMERIC / 60, 2)
                                    AS reductasa_horas,
    (rl.tiempo_reductasa_minutos >= 180)
                                    AS reductasa_cumple,

    -- Resultado y estado
    rl.resultado_validacion         AS resultado_validacion,
    rl.estado_recepcion             AS estado_recepcion,
    rl.observaciones                AS observaciones,

    -- Auditoría
    rl.fecha_hora                   AS timestamp_registro

FROM recepcion_leche rl
INNER JOIN proveedor p ON p.id = rl.proveedor_id
ORDER BY rl.fecha DESC, rl.fecha_hora DESC;