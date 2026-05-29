## Vista 2 — `v_calidad_por_proveedor`
 
### Descripción
Vista de resumen estadístico por proveedor. Agrega todas las recepciones de cada
proveedor para calcular promedios, porcentajes de cumplimiento y tendencias de calidad.
Permite al ingeniero comparar proveedores de forma rápida e identificar cuáles tienen
problemas sistemáticos de calidad.
 
**Hoja de destino en Sheets:** `calidad_por_proveedor`


| Columna | Descripción |
|---|---|
| `proveedor_id` | ID interno del proveedor |
| `proveedor` | Nombre del proveedor |
| `total_recepciones` | Número total de recepciones registradas para este proveedor |
| `total_litros_recibidos` | Total histórico de litros recibidos del proveedor |
| `promedio_litros_por_recepcion` | Promedio de litros por recepción |
| `primera_recepcion` | Fecha de la primera recepción registrada |
| `ultima_recepcion` | Fecha de la recepción más reciente |
| `recepciones_aptas` | Número de recepciones clasificadas como APTA |
| `recepciones_condicionales` | Número de recepciones CONDICIONAL |
| `recepciones_no_aptas` | Número de recepciones NO_APTA |
| `pct_apta` | Porcentaje de recepciones APTA sobre el total |
| `pct_no_apta` | Porcentaje de recepciones NO_APTA sobre el total |
| `ph_promedio` | Valor promedio del pH en todas las recepciones |
| `densidad_promedio` | Valor promedio de la densidad |
| `proteina_promedio_pct` | Porcentaje promedio de proteína |
| `grasa_promedio_pct` | Porcentaje promedio de grasa |
| `solidos_totales_promedio_pct` | Porcentaje promedio de sólidos totales |
| `acidez_promedio` | Valor promedio de la acidez titulable |
| `agua_anadida_promedio_pct` | Porcentaje promedio de agua añadida |
| `crioscopico_promedio` | Valor promedio del punto crioscópico |
| `temperatura_promedio_c` | Temperatura promedio de llegada de la leche |
| `ph_minimo / ph_maximo` | Valores extremos históricos del pH |
| `grasa_minima_pct / grasa_maxima_pct` | Valores extremos históricos de la grasa |
| `proteina_minima_pct / proteina_maxima_pct` | Valores extremos históricos de la proteína |
| `veces_ph_fuera_rango` | Número de veces que el pH estuvo fuera del rango 6.6–6.8 |
| `veces_densidad_fuera_rango` | Veces que la densidad estuvo fuera del rango |
| `veces_grasa_baja` | Veces que la grasa estuvo por debajo del 4.0% |
| `veces_proteina_baja` | Veces que la proteína estuvo por debajo del 3.50% |
| `veces_agua_alta` | Veces que el agua añadida superó el 1% (sospecha de adulteración) |
| `veces_crioscopico_fuera` | Veces que el punto crioscópico salió del rango |
| `veces_alcohol_positivo` | Número de recepciones con prueba de alcohol POSITIVO |
| `reductasa_minutos_promedio` | Duración promedio de la prueba de reductasa en minutos |
| `veces_reductasa_no_cumple` | Número de pruebas que no alcanzaron las 3 horas mínimas |
| `reductasas_pendientes_actuales` | Recepciones que aún tienen la reductasa sin cerrar |
 
---