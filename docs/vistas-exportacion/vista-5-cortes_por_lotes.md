## Vista 5 — `v_cortes_por_lote`
 
### Descripción
Vista complementaria que lista todos los cortes registrados por lote. Se usa junto
con `v_produccion_etapas_completas` para tener el registro completo de la etapa de
cortes, que puede tener N registros por lote y no encaja en el modelo de una fila
por etapa.
 
**Hoja de destino en Sheets:** `cortes`


### Descripción de columnas
 
| Columna | Descripción |
|---|---|
| `lote_id` | ID del lote al que pertenece el corte |
| `codigo_lote` | Código del lote |
| `codigo_producto / nombre_producto` | Tipo de queso fabricado |
| `fecha_produccion` | Fecha de inicio del lote |
| `estado_lote` | Estado actual del lote |
| `corte_id` | ID único del corte |
| `numero_corte` | Número de orden del corte (1, 2, 3...) |
| `hora_corte` | Hora en que se realizó el corte |
| `observacion_corte` | Nota opcional del operario sobre ese corte específico |
| `timestamp_registro` | Cuándo fue registrado en el sistema |
| `total_cortes_en_lote` | Número total de cortes registrados en ese lote |
 
---