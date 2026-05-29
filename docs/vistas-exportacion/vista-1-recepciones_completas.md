## Vista 1 — `v_recepciones_completas`
 
### Descripción
Vista maestra de todas las recepciones de leche. Contiene cada registro completo con
todos los parámetros fisicoquímicos, resultados de validación, estado de la reductasa
e información del proveedor. Es la vista principal para que el ingeniero analice la
calidad de la leche recibida día a día y por proveedor.
 
**Hoja de destino en Sheets:** `recepciones`

| Columna | Descripción |
|---|---|
| `recepcion_id` | Identificador único de la recepción en el sistema |
| `fecha` | Fecha de la recepción (YYYY-MM-DD) |
| `hora_registro` | Hora en que se registró en el sistema (HH:MM) |
| `jornada` | AM o PM según la jornada de recolección |
| `proveedor_id` | ID interno del proveedor |
| `proveedor` | Nombre del proveedor o ruta (ej: ANDRÉS RUTA 1) |
| `tanque_destino` | Tanque donde se almacenó la leche (TANQUE_1, etc.) |
| `litros_recibidos` | Cantidad de leche recibida en litros |
| `registrado_por` | Nombre del operario que registró la recepción |
| `resultado_organoleptico` | CUMPLE o NO_CUMPLE del análisis visual y olfativo |
| `color` | Normal o Anormal — resultado visual del color de la leche |
| `olor` | Normal o Anormal — resultado del olor de la leche |
| `prueba_alcohol` | NEGATIVO (esperado) o POSITIVO (indica problema) |
| `temperatura_c` | Temperatura de la leche al recibirla en °C |
| `densidad_g_ml` | Densidad de la leche en g/mL |
| `ph` | Potencial de hidrógeno de la leche |
| `proteina_pct` | Porcentaje de proteína |
| `grasa_pct` | Porcentaje de grasa |
| `solidos_totales_pct` | Porcentaje de sólidos totales |
| `acidez_titulable` | Acidez titulable de la leche |
| `agua_anadida_pct` | Porcentaje de agua añadida detectada |
| `punto_crioscopico_c` | Punto de congelación de la leche en °C |
| `ph_min / ph_max` | Rango de referencia para el pH (6.6 – 6.8) |
| `densidad_min / densidad_max` | Rango de referencia para la densidad (1.028 – 1.033) |
| `proteina_min` | Mínimo aceptable de proteína (3.50%) |
| `grasa_min` | Mínimo aceptable de grasa (4.0%) |
| `solidos_totales_min` | Mínimo aceptable de sólidos totales (13%) |
| `acidez_min / acidez_max` | Rango de referencia para la acidez (0.16 – 0.18) |
| `agua_max` | Máximo aceptable de agua añadida (< 1%) |
| `crioscopico_min / crioscopico_max` | Rango del punto crioscópico (0.53 – 0.55) |
| `ph_ok` | TRUE si el pH está dentro del rango aceptable |
| `densidad_ok` | TRUE si la densidad está dentro del rango |
| `proteina_ok` | TRUE si la proteína supera el mínimo |
| `grasa_ok` | TRUE si la grasa supera el mínimo |
| `solidos_totales_ok` | TRUE si los sólidos totales superan el mínimo |
| `acidez_ok` | TRUE si la acidez está dentro del rango |
| `agua_ok` | TRUE si el agua añadida está bajo el máximo |
| `crioscopico_ok` | TRUE si el punto crioscópico está dentro del rango |
| `parametros_fuera_rango` | Número total de parámetros que no cumplen el rango (0 = todo OK) |
| `reductasa_hora_inicio` | Hora en que se inició la prueba de azul de metileno |
| `reductasa_hora_fin` | Hora en que se cerró la prueba (puede ser NULL si está pendiente) |
| `reductasa_minutos` | Duración total de la prueba en minutos |
| `reductasa_horas` | Duración total de la prueba en horas (con decimales) |
| `reductasa_cumple` | TRUE si la prueba duró 3 horas o más (≥ 180 minutos) |
| `resultado_validacion` | Clasificación final: APTA, CONDICIONAL o NO_APTA |
| `estado_recepcion` | PENDIENTE_REDUCTASA o COMPLETA |
| `observaciones` | Notas libres registradas por el operario |
| `timestamp_registro` | Fecha y hora exacta del registro en el sistema |
 