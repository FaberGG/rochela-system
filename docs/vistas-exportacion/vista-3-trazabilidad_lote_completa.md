## Vista 3 — `v_trazabilidad_lote_completa`
 
### Descripción
Vista de trazabilidad completa que conecta cada lote de queso con toda la cadena:
lote de leche usado, recepciones que componen ese lote de leche, proveedores de
origen y parámetros de calidad de la materia prima. Permite al ingeniero responder
la pregunta "¿qué leche de qué proveedor entró en este lote de queso?" o al revés,
"¿en qué lotes de queso se usó la leche de este proveedor?".
 
**Hoja de destino en Sheets:** `trazabilidad_lotes`


| Columna | Descripción |
|---|---|
| `lote_id` | ID interno del lote de queso |
| `codigo_lote_queso` | Código del lote (ej: L13226001) |
| `codigo_producto` | Código del producto fabricado (QF001, QF003, etc.) |
| `nombre_producto` | Nombre descriptivo del producto |
| `fecha_inicio_produccion` | Timestamp de inicio del proceso de producción |
| `fecha_produccion` | Fecha de producción (sin hora) |
| `fecha_vencimiento` | Fecha de vencimiento del lote (30 días después) |
| `estado_lote` | Estado actual: FINALIZADO, CANCELADO, o etapa en curso |
| `unidades_producidas` | Unidades de queso producidas al cierre |
| `peso_total_kg` | Peso total producido en kilogramos |
| `rendimiento_general_pct` | Rendimiento calculado: kg queso / litros leche × 100 |
| `rendimiento_teorico_pct` | Rendimiento teórico estimado |
| `fecha_hora_cierre` | Cuándo se cerró el lote |
| `observaciones_lote` | Notas del operario sobre el proceso de producción |
| `lote_leche_id` | ID del lote de leche usado |
| `codigo_lote_leche` | Código del lote de leche (ej: LL-170526-01) |
| `litros_totales_en_mezcla` | Total de litros en el lote de leche mezclado |
| `tanque_proceso` | Tanque donde se hizo la mezcla de leche |
| `mezcla_ph` | pH medido sobre la mezcla total antes de producción |
| `mezcla_grasa_pct` | Grasa de la mezcla (puede diferir de las recepciones individuales) |
| `mezcla_proteina_pct` | Proteína de la mezcla |
| `mezcla_densidad` | Densidad de la mezcla |
| `mezcla_temperatura_c` | Temperatura de la mezcla |
| `mezcla_solidos_totales_pct` | Sólidos totales de la mezcla |
| `mezcla_solidos_no_grasos_pct` | Sólidos no grasos de la mezcla |
| `mezcla_punto_crioscopico` | Punto crioscópico de la mezcla |
| `mezcla_lactosa_pct` | Lactosa de la mezcla |
| `mezcla_agua_anadida_pct` | Agua añadida detectada en la mezcla |
| `observaciones_mezcla` | Notas del operario sobre la preparación de la mezcla |
| `recepcion_id` | ID de la recepción individual que aportó al lote de leche |
| `fecha_recepcion` | Fecha de esa recepción |
| `jornada_recepcion` | AM o PM |
| `litros_aportados_de_esta_recepcion` | Litros de esta recepción que entraron al lote de leche |
| `pct_aporte_al_lote_leche` | Porcentaje que esta recepción representa del total del lote de leche |
| `proveedor_id` | ID del proveedor de esa recepción |
| `proveedor` | Nombre del proveedor |
| `direccion_proveedor` | Dirección o vereda |
| `calidad_recepcion` | Clasificación de la recepción: APTA, CONDICIONAL, NO_APTA |
| `organoleptico` | CUMPLE o NO_CUMPLE del análisis organoléptico de esa recepción |
| `alcohol` | NEGATIVO o POSITIVO de la prueba de alcohol |
| `recepcion_temperatura_c` | Temperatura de la leche al llegar |
| `recepcion_densidad` | Densidad medida en la recepción |
| `recepcion_ph` | pH de la recepción individual |
| `recepcion_proteina_pct` | Proteína de la recepción individual |
| `recepcion_grasa_pct` | Grasa de la recepción individual |
| `recepcion_solidos_totales_pct` | Sólidos totales de la recepción |
| `recepcion_acidez` | Acidez titulable de la recepción |
| `recepcion_agua_anadida_pct` | Agua añadida en la recepción |
| `recepcion_punto_crioscopico` | Punto crioscópico de la recepción |
| `recepcion_reductasa_minutos` | Duración de la reductasa de esa recepción |
| `recepcion_reductasa_cumple` | TRUE si la reductasa de esa recepción cumple ≥ 3 horas |
| `observaciones_recepcion` | Notas del operario sobre esa recepción |
 
---