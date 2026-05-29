
## Vista 4 — `v_produccion_etapas_completas`
 
### Descripción
Vista detallada de cada lote de producción con todas sus etapas registradas,
incluyendo variables de control por etapa (temperatura, horas, insumos, cantidades)
y los datos del cierre. Permite al ingeniero analizar la consistencia del proceso
productivo y correlacionarla con la calidad de la materia prima.
 
**Hoja de destino en Sheets:** `produccion_etapas`


| Columna | Descripción |
|---|---|
| `lote_id` | ID interno del lote |
| `codigo_lote` | Código del lote (L13226001) |
| `codigo_producto / nombre_producto` | Identificación del tipo de queso fabricado |
| `fecha_vencimiento` | Fecha de vencimiento del lote |
| `fecha_produccion` | Fecha de inicio de producción |
| `hora_inicio` | Hora de inicio del proceso |
| `estado_actual` | Estado actual del lote |
| `codigo_lote_leche` | Código del lote de leche usado como materia prima |
| `litros_totales_mezcla` | Litros totales del lote de leche que se procesó |
| `etapa_id` | ID de la etapa registrada |
| `tipo_etapa` | Nombre de la etapa (PASTEURIZACION, CLORURO, CUAJO, etc.) |
| `hora_etapa` | Hora operativa en que ocurrió esa etapa |
| `timestamp_registro_etapa` | Fecha y hora exacta en que el operario la registró |
| `pasteurizacion_temperatura_c` | Temperatura alcanzada en la pasteurización (NULL si no aplica) |
| `cloruro_temperatura_c` | Temperatura al agregar el cloruro |
| `cloruro_gramos` | Cantidad de cloruro agregada en gramos |
| `cloruro_lote_insumo` | Código del lote del cloruro usado (trazabilidad de insumos) |
| `cuajo_temperatura_c` | Temperatura al agregar el cuajo |
| `cuajo_gramos` | Cantidad de cuajo agregada en gramos |
| `cuajo_lote_insumo` | Código del lote del cuajo usado |
| `lavado_desuerado_litros` | Litros del lavado/desuerado (NULL si la etapa no aplica al producto) |
| `desuerado_litros` | Litros obtenidos en el desuerado |
| `salado_temperatura_c` | Temperatura durante el salado |
| `salado_kg` | Kilogramos de sal usados |
| `sodio_inicial_pct` | Porcentaje de sodio al inicio del salado |
| `sodio_final_pct` | Porcentaje de sodio al final del salado |
| `diferencia_sodio_pct` | Diferencia de sodio entre inicio y fin (absorción) |
| `sal_lote_insumo` | Código del lote del bulto de sal (trazabilidad de insumos) |
| `prensado_hora_fin` | Hora en que terminó el prensado |
| `prensado_duracion_minutos` | Duración total del prensado en minutos |
| `prensado_duracion_horas` | Duración total del prensado en horas |
| `prensado_presion_psi` | Presión aplicada durante el prensado en PSI |
| `prensado_responsable` | Nombre del operario responsable del prensado |
| `unidades_producidas` | Número de unidades de queso obtenidas |
| `peso_total_kg` | Peso total del queso producido |
| `rendimiento_general_pct` | Rendimiento real: kg queso / litros leche × 100 |
| `rendimiento_teorico_pct` | Rendimiento teórico estimado |
| `fecha_hora_cierre` | Cuándo se cerró el lote |
| `observaciones_lote` | Notas del operario sobre el proceso |