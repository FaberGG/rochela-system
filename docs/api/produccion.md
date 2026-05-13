# API - Produccion (Lotes)

Fuente central: `docs/api/api-docs.json` (Swagger).

## Convenciones

- Base URL: `/api/v1`
- Formato: JSON
- Errores estandar: ver `docs/api/errores.md`

## Endpoints

### GET `/lotes`

Lista lotes con filtros opcionales.

**Query params**

| Parametro | Tipo | Descripcion |
| --- | --- | --- |
| `estado` | string | Estado actual del lote |
| `productoCodigo` | string | Codigo del producto |
| `desde` | string (date) | Fecha inicial (YYYY-MM-DD) |
| `hasta` | string (date) | Fecha final (YYYY-MM-DD) |
| `soloActivos` | boolean | Si es true, solo lotes activos |

**Response 200**

- `application/json`: array de `LoteResumenResponse`

**Ejemplo response 200**

```json
[
  {
    "id": 1,
    "codigoLote": "L-20260513-01",
    "producto": {
      "codigo": "QUESO-001",
      "nombre": "Queso campesino"
    },
    "fechaHoraInicio": "2026-05-13T08:30:00",
    "fechaVencimiento": "2026-06-13",
    "estadoActual": "INICIADO",
    "siguienteEtapa": "PASTEURIZACION"
  }
]
```

---

### POST `/lotes`

Crea un nuevo lote de produccion.

**Request body**

- `application/json`: `LoteCreateRequest`

**Ejemplo request**

```json
{
  "productoCodigo": "QUESO-001",
  "fechaHoraInicio": "2026-05-13T08:30:00",
  "recepcionLecheId": 25
}
```

**Response 201**

- `application/json`: `LoteResumenResponse`

**Ejemplo response 201**

```json
{
  "id": 1,
  "codigoLote": "L-20260513-01",
  "producto": {
    "codigo": "QUESO-001",
    "nombre": "Queso campesino"
  },
  "fechaHoraInicio": "2026-05-13T08:30:00",
  "fechaVencimiento": "2026-06-13",
  "estadoActual": "INICIADO",
  "siguienteEtapa": "PASTEURIZACION"
}
```

**Response 400**

- `application/json`: `ErrorResponse`

---

### GET `/lotes/{id}`

Devuelve el detalle completo del lote.

**Path params**

| Parametro | Tipo | Descripcion |
| --- | --- | --- |
| `id` | integer (int64) | Id del lote |

**Response 200**

- `application/json`: `LoteDetalleResponse`

**Ejemplo response 200**

```json
{
  "id": 1,
  "codigoLote": "L-20260513-01",
  "producto": {
    "codigo": "QUESO-001",
    "nombre": "Queso campesino"
  },
  "fechaHoraInicio": "2026-05-13T08:30:00",
  "fechaVencimiento": "2026-06-13",
  "estadoActual": "INICIADO",
  "siguienteEtapa": "PASTEURIZACION",
  "etapas": [
    {
      "tipoEtapa": "PASTEURIZACION",
      "hora": "08:45:00",
      "fechaHoraRegistro": "2026-05-13T08:45:00",
      "temperatura": 72.5
    }
  ],
  "cortes": [
    {
      "numeroCorte": 1,
      "hora": "10:15:00",
      "observacion": "Corte uniforme"
    }
  ],
  "cierre": {
    "codigoLote": "L-132261-01",
    "estadoActual": "FINALIZADO",
    "fechaHoraCierre": "2026-05-13T13:00:00",
    "unidadesProducidas": 250,
    "pesoTotalKg": 120.5,
    "rendimientoTeorico": 85.0,
    "rendimientoGeneral": 82.5
  }
}
```

**Response 404**

- `application/json`: `ErrorResponse`

---

### PATCH `/lotes/{id}/cancelar`

Cancela un lote en proceso.

**Path params**

| Parametro | Tipo | Descripcion |
| --- | --- | --- |
| `id` | integer (int64) | Id del lote |

**Response 200**

- `application/json`: `LoteResumenResponse`

**Ejemplo response 200**

```json
{
  "id": 1,
  "codigoLote": "L-20260513-01",
  "producto": {
    "codigo": "QUESO-001",
    "nombre": "Queso campesino"
  },
  "fechaHoraInicio": "2026-05-13T08:30:00",
  "fechaVencimiento": "2026-06-13",
  "estadoActual": "CANCELADO",
  "siguienteEtapa": "CANCELADO"
}
```

**Response 404**

- `application/json`: `ErrorResponse`

**Response 409**

- `application/json`: `ErrorResponse`

---

### POST `/lotes/{id}/pasteurizacion`

Registra la etapa de pasteurizacion.

**Path params**

| Parametro | Tipo | Descripcion |
| --- | --- | --- |
| `id` | integer (int64) | Id del lote |

**Request body**

- `application/json`: `PasteurizacionRequest`

**Ejemplo request**

```json
{
  "hora": "08:45:00",
  "temperatura": 72.5
}
```

**Response 201**

- `application/json`: `LoteResumenResponse`

**Ejemplo response 201**

```json
{
  "id": 1,
  "codigoLote": "L-20260513-01",
  "producto": {
    "codigo": "QUESO-001",
    "nombre": "Queso campesino"
  },
  "fechaHoraInicio": "2026-05-13T08:30:00",
  "fechaVencimiento": "2026-06-13",
  "estadoActual": "PASTEURIZACION",
  "siguienteEtapa": "CLORURO"
}
```

**Response 404**

- `application/json`: `ErrorResponse`

**Response 409**

- `application/json`: `ErrorResponse`

---

### POST `/lotes/{id}/cloruro`

Registra la etapa de cloruro.

**Path params**

| Parametro | Tipo | Descripcion |
| --- | --- | --- |
| `id` | integer (int64) | Id del lote |

**Request body**

- `application/json`: `CloruroRequest`

**Ejemplo request**

```json
{
  "hora": "09:00:00",
  "temperatura": 35.0,
  "cantidadGramos": 12.0,
  "loteCloruro": "CL-2026-05"
}
```

**Response 201**

- `application/json`: `LoteResumenResponse`

**Ejemplo response 201**

```json
{
  "id": 1,
  "codigoLote": "L-20260513-01",
  "producto": {
    "codigo": "QUESO-001",
    "nombre": "Queso campesino"
  },
  "fechaHoraInicio": "2026-05-13T08:30:00",
  "fechaVencimiento": "2026-06-13",
  "estadoActual": "CLORURO",
  "siguienteEtapa": "CUAJO"
}
```

**Response 404**

- `application/json`: `ErrorResponse`

**Response 409**

- `application/json`: `ErrorResponse`

---

### POST `/lotes/{id}/cuajo`

Registra la etapa de cuajo.

**Path params**

| Parametro | Tipo | Descripcion |
| --- | --- | --- |
| `id` | integer (int64) | Id del lote |

**Request body**

- `application/json`: `CuajoRequest`

**Ejemplo request**

```json
{
  "hora": "09:15:00",
  "temperatura": 32.0,
  "cantidadGramos": 8.0,
  "loteCuajo": "CU-2026-05"
}
```

**Response 201**

- `application/json`: `LoteResumenResponse`

**Ejemplo response 201**

```json
{
  "id": 1,
  "codigoLote": "L-20260513-01",
  "producto": {
    "codigo": "QUESO-001",
    "nombre": "Queso campesino"
  },
  "fechaHoraInicio": "2026-05-13T08:30:00",
  "fechaVencimiento": "2026-06-13",
  "estadoActual": "CUAJO",
  "siguienteEtapa": "CORTES"
}
```

**Response 404**

- `application/json`: `ErrorResponse`

**Response 409**

- `application/json`: `ErrorResponse`

---

### POST `/lotes/{id}/cortes`

Agrega un corte al lote.

**Path params**

| Parametro | Tipo | Descripcion |
| --- | --- | --- |
| `id` | integer (int64) | Id del lote |

**Request body**

- `application/json`: `CorteCreateRequest`

**Ejemplo request**

```json
{
  "hora": "10:15:00",
  "observacion": "Corte uniforme"
}
```

**Response 201**

- `application/json`: `CorteCreateResponse`

**Ejemplo response 201**

```json
{
  "numeroCorte": 2,
  "hora": "10:15:00",
  "observacion": "Corte uniforme",
  "totalCortesRegistrados": 2
}
```

**Response 404**

- `application/json`: `ErrorResponse`

**Response 409**

- `application/json`: `ErrorResponse`

---

### POST `/lotes/{id}/cortes/cerrar`

Finaliza la etapa de cortes.

**Path params**

| Parametro | Tipo | Descripcion |
| --- | --- | --- |
| `id` | integer (int64) | Id del lote |

**Response 200**

- `application/json`: `LoteResumenResponse`

**Ejemplo response 200**

```json
{
  "id": 1,
  "codigoLote": "L-20260513-01",
  "producto": {
    "codigo": "QUESO-001",
    "nombre": "Queso campesino"
  },
  "fechaHoraInicio": "2026-05-13T08:30:00",
  "fechaVencimiento": "2026-06-13",
  "estadoActual": "CORTES",
  "siguienteEtapa": "LAVADO_DESUERADO"
}
```

**Response 404**

- `application/json`: `ErrorResponse`

**Response 409**

- `application/json`: `ErrorResponse`

---

### POST `/lotes/{id}/lavado-desuerado`

Registra la etapa de lavado y desuerado.

**Path params**

| Parametro | Tipo | Descripcion |
| --- | --- | --- |
| `id` | integer (int64) | Id del lote |

**Request body**

- `application/json`: `LavadoDesueradoRequest`

**Ejemplo request**

```json
{
  "hora": "10:00:00",
  "litros": 150.0
}
```

**Response 201**

- `application/json`: `LoteResumenResponse`

**Ejemplo response 201**

```json
{
  "id": 1,
  "codigoLote": "L-20260513-01",
  "producto": {
    "codigo": "QUESO-001",
    "nombre": "Queso campesino"
  },
  "fechaHoraInicio": "2026-05-13T08:30:00",
  "fechaVencimiento": "2026-06-13",
  "estadoActual": "LAVADO_DESUERADO",
  "siguienteEtapa": "DESUERADO"
}
```

**Response 404**

- `application/json`: `ErrorResponse`

**Response 409**

- `application/json`: `ErrorResponse`

---

### POST `/lotes/{id}/desuerado`

Registra la etapa de desuerado.

**Path params**

| Parametro | Tipo | Descripcion |
| --- | --- | --- |
| `id` | integer (int64) | Id del lote |

**Request body**

- `application/json`: `DesueradoRequest`

**Ejemplo request**

```json
{
  "hora": "10:30:00",
  "litros": 120.0
}
```

**Response 201**

- `application/json`: `LoteResumenResponse`

**Ejemplo response 201**

```json
{
  "id": 1,
  "codigoLote": "L-20260513-01",
  "producto": {
    "codigo": "QUESO-001",
    "nombre": "Queso campesino"
  },
  "fechaHoraInicio": "2026-05-13T08:30:00",
  "fechaVencimiento": "2026-06-13",
  "estadoActual": "DESUERADO",
  "siguienteEtapa": "SALADO"
}
```

**Response 404**

- `application/json`: `ErrorResponse`

**Response 409**

- `application/json`: `ErrorResponse`

---

### POST `/lotes/{id}/salado`

Registra la etapa de salado.

**Path params**

| Parametro | Tipo | Descripcion |
| --- | --- | --- |
| `id` | integer (int64) | Id del lote |

**Request body**

- `application/json`: `SaladoRequest`

**Ejemplo request**

```json
{
  "hora": "11:00:00",
  "temperatura": 12.0,
  "cantidadKg": 20.0,
  "sodioInicial": 1.2,
  "sodioFinal": 1.8,
  "loteSal": "SAL-2026-05"
}
```

**Response 201**

- `application/json`: `LoteResumenResponse`

**Ejemplo response 201**

```json
{
  "id": 1,
  "codigoLote": "L-20260513-01",
  "producto": {
    "codigo": "QUESO-001",
    "nombre": "Queso campesino"
  },
  "fechaHoraInicio": "2026-05-13T08:30:00",
  "fechaVencimiento": "2026-06-13",
  "estadoActual": "SALADO",
  "siguienteEtapa": "PRENSADO"
}
```

**Response 404**

- `application/json`: `ErrorResponse`

**Response 409**

- `application/json`: `ErrorResponse`

---

### POST `/lotes/{id}/prensado`

Registra la etapa de prensado.

**Path params**

| Parametro | Tipo | Descripcion |
| --- | --- | --- |
| `id` | integer (int64) | Id del lote |

**Request body**

- `application/json`: `PrensadoRequest`

**Ejemplo request**

```json
{
  "horaInicio": "11:30:00",
  "horaFin": "12:00:00",
  "presionPsi": 20.0,
  "responsable": "Operario 1"
}
```

**Response 201**

- `application/json`: `LoteResumenResponse`

**Ejemplo response 201**

```json
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
  "siguienteEtapa": "FINALIZADO"
}
```

**Response 404**

- `application/json`: `ErrorResponse`

**Response 409**

- `application/json`: `ErrorResponse`

---

### POST `/lotes/{id}/cierre`

Registra el cierre del lote y calcula rendimientos.

**Path params**

| Parametro | Tipo | Descripcion |
| --- | --- | --- |
| `id` | integer (int64) | Id del lote |

**Request body**

- `application/json`: `CierreLoteRequest`

**Ejemplo request**

```json
{
  "unidadesProducidas": 250,
  "pesoTotalKg": 120.5,
  "observaciones": "Proceso estable"
}
```

**Response 200**

- `application/json`: `CierreLoteResponse`

**Ejemplo response 200**

```json
{
  "codigoLote": "L-132261-01",
  "estadoActual": "FINALIZADO",
  "fechaHoraCierre": "2026-05-13T13:00:00",
  "unidadesProducidas": 250,
  "pesoTotalKg": 120.5,
  "rendimientoTeorico": 85.0,
  "rendimientoGeneral": 82.5
}
```

**Response 404**

- `application/json`: `ErrorResponse`

**Response 409**

- `application/json`: `ErrorResponse`

## Esquemas (resumen)

### `LoteCreateRequest`

- `productoCodigo` (string)
- `fechaHoraInicio` (date-time)
- `recepcionLecheId` (int64)

### `LoteResumenResponse`

- `id` (int64)
- `codigoLote` (string)
- `producto` (ProductoResumen)
- `fechaHoraInicio` (date-time)
- `fechaVencimiento` (date)
- `estadoActual` (string)
- `siguienteEtapa` (string)

### `ProductoResumen`

- `codigo` (string)
- `nombre` (string)

### `PasteurizacionRequest`

- `hora` (string)
- `temperatura` (double)

### `CloruroRequest`

- `hora` (string)
- `temperatura` (double)
- `cantidadGramos` (double)
- `loteCloruro` (string)

### `CuajoRequest`

- `hora` (string)
- `temperatura` (double)
- `cantidadGramos` (double)
- `loteCuajo` (string)

### `CorteCreateRequest`

- `hora` (string)
- `observacion` (string)

### `CorteCreateResponse`

- `numeroCorte` (int32)
- `hora` (string)
- `observacion` (string)
- `totalCortesRegistrados` (int64)

### `LavadoDesueradoRequest`

- `hora` (string)
- `litros` (double)

### `DesueradoRequest`

- `hora` (string)
- `litros` (double)

### `SaladoRequest`

- `hora` (string)
- `temperatura` (double)
- `cantidadKg` (double)
- `sodioInicial` (double)
- `sodioFinal` (double)
- `loteSal` (string)

### `PrensadoRequest`

- `horaInicio` (string)
- `horaFin` (string)
- `presionPsi` (double)
- `responsable` (string)

### `CierreLoteRequest`

- `unidadesProducidas` (int32)
- `pesoTotalKg` (double)
- `observaciones` (string)

### `CierreLoteResponse`

- `codigoLote` (string)
- `estadoActual` (string)
- `fechaHoraCierre` (date-time)
- `unidadesProducidas` (int32)
- `pesoTotalKg` (double)
- `rendimientoTeorico` (double)
- `rendimientoGeneral` (double)

### `LoteDetalleResponse`

- `id` (int64)
- `codigoLote` (string)
- `producto` (ProductoResumen)
- `fechaHoraInicio` (date-time)
- `fechaVencimiento` (date)
- `estadoActual` (string)
- `siguienteEtapa` (string)
- `etapas` (array de EtapaDetalle)
- `cortes` (array de CorteDetalle)
- `cierre` (CierreLoteResponse)

