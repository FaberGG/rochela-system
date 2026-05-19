# Usuarios y flujos

El prototipo considera dos perfiles de usuario. No se implementa autenticación en esta fase; los perfiles se mencionan para delimitar responsabilidades y orientar el diseño de pantallas en el frontend.

## Operario de Producción

Es quien ejecuta físicamente el proceso en planta y registra los datos en el sistema.

**Flujo principal — Seguimiento de loteQueso:**

```
1. Ve la lista de loteQuesos activos con su estado actual
2. Selecciona un loteQueso
3. Ve el detalle del loteQueso: datos generales, historial de etapas registradas
4. Registra los datos de la etapa en curso
5. El sistema avanza el loteQueso al siguiente estado
6. Puede agregar cortes adicionales mientras el loteQueso está en estado CORTES
7. Al finalizar, registra unidades producidas y peso total
```

**Flujo secundario — Recepción de leche:**

```
1. Crea un nuevo registro de recepción
2. Selecciona el proveedor y la ubicación del tanque
3. Ingresa los parámetros fisicoquímicos medidos
4. El sistema valida cada parámetro contra los rangos aceptables
5. El registro queda guardado y visible para el ingeniero
```

## Ingeniero de Producción y Calidad

Supervisa los procesos, analiza la información registrada y genera reportes.

**Flujo principal — Consulta de loteQuesos:**

```
1. Ve todos los loteQuesos: activos, finalizados y cancelados
2. Puede filtrar por fecha, producto o estado
3. Selecciona un loteQueso y ve el detalle completo con todas las etapas
4. Consulta los índices de rendimiento del loteQueso finalizado
5. Exporta los datos a Google Sheets
```

**Flujo secundario — Revisión de recepciones de leche:**

```
1. Ve la lista de recepciones registradas
2. Selecciona una recepción y ve los parámetros con indicadores de rango
3. Exporta el historial de recepciones a Google Sheets
```

