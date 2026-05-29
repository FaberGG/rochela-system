CREATE OR REPLACE VIEW v_cortes_por_lote AS
SELECT
    l.id                            AS lote_id,
    l.codigo_lote                   AS codigo_lote,
    pr.codigo                       AS codigo_producto,
    pr.nombre                       AS nombre_producto,
    DATE(l.fecha_hora_inicio)       AS fecha_produccion,
    l.estado_actual                 AS estado_lote,
 
    -- Datos del corte
    c.id                            AS corte_id,
    c.numero_corte                  AS numero_corte,
    c.hora                          AS hora_corte,
    c.observacion                   AS observacion_corte,
    c.fecha_hora_registro           AS timestamp_registro,
 
    -- Conteo total de cortes en ese lote
    COUNT(c2.id) OVER (PARTITION BY l.id)
                                    AS total_cortes_en_lote
 
FROM lote l
INNER JOIN producto pr ON pr.id = l.producto_id
INNER JOIN corte c     ON c.lote_id = l.id
INNER JOIN corte c2    ON c2.lote_id = l.id
ORDER BY l.fecha_hora_inicio DESC, c.numero_corte ASC;