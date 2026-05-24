# API - Operario

Fuente central: `docs/api/api-docs.json` (Swagger).

## Convenciones

- Base URL: `/api/v1`
- Formato: JSON
- Errores estandar: ver `docs/api/errores.md`

## Endpoints

### GET `/operario/feed`

Devuelve el feed agregado para operario.

**Response 200**

- `application/json`: `OperarioFeedResponse`

**Ejemplo response 200**

```json
{
  "pendientes": [
    {
      "id": 1,
      "fecha": "2026-05-13",
      "fechaHora": "2026-05-13T09:05:00",
      "jornada": "AM",
      "proveedor": "Lacteos del Norte",
      "horaInicioReductasa": "09:00:00",
      "minutosTranscurridos": 120
    }
  ],
  "lotesActivos": [
    {
      "id": 1,
      "codigoLote": "L-20260513-01",
      "producto": {
        "codigo": "QUESO-001",
        "nombre": "Queso campesino"
      },
      "fechaHoraInicio": "2026-05-13T08:30:00",
      "fechaVencimiento": "2026-06-13",
      "estadoActual": "PRENSADO",
      "siguienteEtapa": "FINALIZADO",
      "etapaActualInicio": "2026-05-13T11:30:00",
      "porcentajeCompletado": 70.0
    }
  ]
}
```

## Esquemas (resumen)

### `OperarioFeedResponse`

- `pendientes` (array de RecepcionPendiente)
- `lotesActivos` (array de LoteResumenResponse)

