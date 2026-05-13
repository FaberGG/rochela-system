# API - Recepcion

Fuente central: `docs/api/api-docs.json` (Swagger).

## Convenciones

- Base URL: `/api/v1`
- Formato: JSON
- Errores estandar: ver `docs/api/errores.md`

## Endpoints

### GET `/recepciones`

Lista recepciones con filtros opcionales.

**Query params**

| Parametro | Tipo | Descripcion |
| --- | --- | --- |
| `proveedorId` | integer (int64) | Id del proveedor |
| `desde` | string (date) | Fecha inicial (YYYY-MM-DD) |
| `hasta` | string (date) | Fecha final (YYYY-MM-DD) |
| `resultado` | string | `APTA`, `NO_APTA`, `CONDICIONAL` |

**Response 200**

- `application/json`: array de `RecepcionLeche`

**Ejemplo response 200**

```json
[
  {
	"id": 1,
	"fecha": "2026-05-13",
	"fechaHora": "2026-05-13T09:00:00",
	"proveedorId": 10,
	"jornada": "AM",
	"ubicacion": "TANQUE_1",
	"cantidadLitros": 150.0,
	"realizadoPor": "Operario 1",
	"analisisOrganoleptico": "CUMPLE",
	"colorCumple": true,
	"olorCumple": true,
	"alcohol": "NEGATIVO",
	"temperatura": 8.5,
	"densidad": 1.03,
	"ph": 6.6,
	"proteina": 3.2,
	"grasa": 3.5,
	"solidosTotales": 12.3,
	"acidezTitulable": 0.13,
	"aguaAnadida": 0.0,
	"puntoCrioscopico": -0.52,
	"horaInicioReductasa": "09:00:00",
	"horaFinReductasa": "11:30:00",
	"tiempoReductasaMinutos": 150,
	"resultadoValidacion": "APTA",
	"estadoRecepcion": "COMPLETA",
	"observaciones": "Sin novedades"
  }
]
```

---

### POST `/recepciones`

Registra una nueva recepcion de leche.

**Request body**

- `application/json`: `RecepcionCreateRequest`

**Ejemplo request**

```json
{
  "fecha": "2026-05-13",
  "proveedorId": 10,
  "jornada": "AM",
  "ubicacion": "TANQUE_1",
  "cantidadLitros": 150.0,
  "realizadoPor": "Operario 1",
  "analisisOrganoleptico": "CUMPLE",
  "colorCumple": true,
  "olorCumple": true,
  "alcohol": "NEGATIVO",
  "temperatura": 8.5,
  "densidad": 1.03,
  "ph": 6.6,
  "proteina": 3.2,
  "grasa": 3.5,
  "solidosTotales": 12.3,
  "acidezTitulable": 0.13,
  "aguaAnadida": 0.0,
  "puntoCrioscopico": -0.52,
  "horaInicioReductasa": "09:00:00",
  "observaciones": "Sin novedades"
}
```

**Response 201**

- `application/json`: `RecepcionResponse`

**Ejemplo response 201**

```json
{
  "id": 1,
  "estadoRecepcion": "PENDIENTE_REDUCTASA",
  "resultadoValidacion": "APTA",
  "validacionDetalle": {
	"ph": {
	  "valor": 6.6,
	  "rangoMin": 6.6,
	  "rangoMax": 6.8,
	  "apto": true
	}
  }
}
```

**Response 400**

- `application/json`: `ErrorResponse`

---

### GET `/recepciones/{id}`

Devuelve el detalle de validaciones de una recepcion.

**Path params**

| Parametro | Tipo | Descripcion |
| --- | --- | --- |
| `id` | integer (int64) | Id de la recepcion |

**Response 200**

- `application/json`: `RecepcionResponse`

**Ejemplo response 200**

```json
{
  "id": 1,
  "estadoRecepcion": "COMPLETA",
  "resultadoValidacion": "APTA",
  "validacionDetalle": {
	"proteina": {
	  "valor": 3.2,
	  "rangoMin": 3.0,
	  "rangoMax": 3.8,
	  "apto": true
	}
  }
}
```

**Response 404**

- `application/json`: `ErrorResponse`

---

### PATCH `/recepciones/{id}/reductasa`

Cierra la prueba de reductasa.

**Path params**

| Parametro | Tipo | Descripcion |
| --- | --- | --- |
| `id` | integer (int64) | Id de la recepcion |

**Request body**

- `application/json`: `RecepcionReductasaRequest`

**Ejemplo request**

```json
{
  "horaFinReductasa": "11:30:00"
}
```

**Response 200**

- `application/json`: `RecepcionReductasaResponse`

**Ejemplo response 200**

```json
{
  "id": 1,
  "horaInicioReductasa": "09:00:00",
  "horaFinReductasa": "11:30:00",
  "tiempoReductasaMinutos": 150,
  "cumpleReductasa": true,
  "estadoRecepcion": "COMPLETA",
  "resultadoValidacion": "APTA"
}
```

**Response 404**

- `application/json`: `ErrorResponse`

**Response 409**

- `application/json`: `ErrorResponse`

---

### GET `/recepciones/pendientes`

Lista recepciones con reductasa pendiente.

**Response 200**

- `application/json`: array de `RecepcionPendiente`

**Ejemplo response 200**

```json
[
  {
	"id": 1,
	"fecha": "2026-05-13",
	"jornada": "AM",
	"proveedor": "Lacteos del Norte",
	"horaInicioReductasa": "09:00:00",
	"minutosTranscurridos": 120
  }
]
```

## Esquemas (resumen)

### `RecepcionCreateRequest`

- `fecha` (date)
- `proveedorId` (int64)
- `jornada` (string: `AM`, `PM`)
- `ubicacion` (string: `TANQUE_1`, `TANQUE_2`, `TANQUE_3`, `CUARTO_FRIO`, `PROCESO`)
- `cantidadLitros` (double)
- `realizadoPor` (string)
- `analisisOrganoleptico` (string: `CUMPLE`, `NO_CUMPLE`)
- `colorCumple` (boolean)
- `olorCumple` (boolean)
- `alcohol` (string: `POSITIVO`, `NEGATIVO`)
- `temperatura` (double)
- `densidad` (double)
- `ph` (double)
- `proteina` (double)
- `grasa` (double)
- `solidosTotales` (double)
- `acidezTitulable` (double)
- `aguaAnadida` (double)
- `puntoCrioscopico` (double)
- `horaInicioReductasa` (string)
- `observaciones` (string)

### `RecepcionResponse`

- `id` (int64)
- `estadoRecepcion` (string: `PENDIENTE_REDUCTASA`, `COMPLETA`)
- `resultadoValidacion` (string: `APTA`, `NO_APTA`, `CONDICIONAL`)
- `validacionDetalle` (map<string, ValidacionDetalle>)

### `ValidacionDetalle`

- `valor` (double)
- `rangoMin` (double)
- `rangoMax` (double)
- `apto` (boolean)

### `RecepcionReductasaRequest`

- `horaFinReductasa` (string)

### `RecepcionReductasaResponse`

- `id` (int64)
- `horaInicioReductasa` (string)
- `horaFinReductasa` (string)
- `tiempoReductasaMinutos` (int32)
- `cumpleReductasa` (boolean)
- `estadoRecepcion` (string: `PENDIENTE_REDUCTASA`, `COMPLETA`)
- `resultadoValidacion` (string: `APTA`, `NO_APTA`, `CONDICIONAL`)

### `RecepcionPendiente`

- `id` (int64)
- `fecha` (date)
- `jornada` (string: `AM`, `PM`)
- `proveedor` (string)
- `horaInicioReductasa` (string)
- `minutosTranscurridos` (int64)

### `RecepcionLeche`

- `id` (int64)
- `fecha` (date)
- `fechaHora` (date-time)
- `proveedorId` (int64)
- `jornada` (string: `AM`, `PM`)
- `ubicacion` (string: `TANQUE_1`, `TANQUE_2`, `TANQUE_3`, `CUARTO_FRIO`, `PROCESO`)
- `cantidadLitros` (double)
- `realizadoPor` (string)
- `analisisOrganoleptico` (string: `CUMPLE`, `NO_CUMPLE`)
- `colorCumple` (boolean)
- `olorCumple` (boolean)
- `alcohol` (string: `POSITIVO`, `NEGATIVO`)
- `temperatura` (double)
- `densidad` (double)
- `ph` (double)
- `proteina` (double)
- `grasa` (double)
- `solidosTotales` (double)
- `acidezTitulable` (double)
- `aguaAnadida` (double)
- `puntoCrioscopico` (double)
- `horaInicioReductasa` (string)
- `horaFinReductasa` (string)
- `tiempoReductasaMinutos` (int32)
- `resultadoValidacion` (string: `APTA`, `NO_APTA`, `CONDICIONAL`)
- `estadoRecepcion` (string: `PENDIENTE_REDUCTASA`, `COMPLETA`)
- `observaciones` (string)

