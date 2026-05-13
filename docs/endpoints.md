
## 3. Endpoints

### Convenciones

- Base URL: `/api/v1`
- Formato: JSON
- Timestamps: ISO 8601 (`2026-05-12T14:30:00`)
- Errores estándar:

```json
{
  "status": 400,
  "error": "ESTADO_INVALIDO",
  "mensaje": "El lote L01261 está en estado SALADO y no puede registrar etapa CUAJO"
}
```

---

### 3.1 Productos

| Método | Endpoint | Descripción |
| --- | --- | --- |
| `GET` | `/productos` | Lista todos los productos del catálogo |
| `GET` | `/productos/{codigo}` | Detalle de un producto con sus etapas configuradas |

---

### 3.2 Proveedores

| Método | Endpoint | Descripción |
| --- | --- | --- |
| `GET` | `/proveedores` | Lista todos los proveedores activos |
| `GET` | `/proveedores/{id}` | Detalle de un proveedor |
| `POST` | `/proveedores` | Crea un nuevo proveedor |
| `PUT` | `/proveedores/{id}` | Actualiza un proveedor |
| `DELETE` | `/proveedores/{id}` | Soft delete (marca `activo = false`) |

**Body `POST /proveedores`:**

```json
{
  "nombreEmpresa": "Calidad Rochela",
  "nombrePropietario": "Juan Pérez",
  "nombreMayordomo": "Carlos López",
  "telefono": "3001234567",
  "correo": "rochela@example.com",
  "direccion": "Vereda El Recreo"
}
```

---

### 3.3 Recepción de Leche

| Método | Endpoint | Descripción |
| --- | --- | --- |
| `GET` | `/recepciones` | Lista recepciones con filtros opcionales |
| `GET` | `/recepciones/{id}` | Detalle de una recepción con validación por parámetro |
| `POST` | `/recepciones` | Registra una nueva recepción y valida rangos automáticamente |
| `PATCH` | `/recepciones/{id}/reductasa` | Registra la hora final y calcula el tiempo |

**Query params `GET /recepciones`:**

| Parámetro | Tipo | Descripción |
| --- | --- | --- |
| `proveedorId` | Long | Filtra por proveedor |
| `desde` | Date | Fecha inicial (`yyyy-MM-dd`) |
| `hasta` | Date | Fecha final |
| `resultado` | String | `APTA`, `NO_APTA`, `CONDICIONAL` |

**Body `POST /recepciones`:**

```json
{
  "proveedorId": 3,
  "loteTanque": "T001",
  "ubicacion": "TANQUE_1",
  "cantidadLitros": 450.5,
  "grasa": 4.2,
  "solidos": 12.8,
  "proteina": 3.6,
  "puntoCongelacion": -0.54,
  "temperatura": 6.0,
  "densidad": 1.030,
  "lactosa": 4.5,
  "solidosTotales": 13.2,
  "aguaAnadida": 0.5,
  "ph": 6.7,
  "observaciones": "Leche llegó en buen estado"
}
```

**Response `POST /recepciones`** — incluye el resultado de validación por parámetro:

```json
{
  "id": 12,
  "codigoLote": "T001",
  "resultadoValidacion": "APTA",
  "validacionDetalle": {
    "grasa":           { "valor": 4.2,   "rangoMin": 4.0,  "rangoMax": null, "apto": true  },
    "ph":              { "valor": 6.7,   "rangoMin": 6.6,  "rangoMax": 6.8,  "apto": true  },
    "aguaAnadida":     { "valor": 0.5,   "rangoMin": null, "rangoMax": 1.0,  "apto": true  },
    "solidosTotales":  { "valor": 13.2,  "rangoMin": 13.0, "rangoMax": null, "apto": true  }
  }
}
```

Y el endpoint adicional

| Método | Endpoint | Descripción |
| --- | --- | --- |
| `PATCH` | `/recepciones/{id}/reductasa` | Registra la hora final y calcula el tiempo |

json `{ "horaFinReductasa": "14:30:00" }`

---

### 3.4 Lotes — Gestión General

| Método | Endpoint | Descripción |
| --- | --- | --- |
| `GET` | `/lotes` | Lista lotes con filtros opcionales |
| `GET` | `/lotes/{id}` | Detalle completo del lote con historial de etapas |
| `POST` | `/lotes` | Crea un nuevo lote y genera el código automáticamente |
| `PATCH` | `/lotes/{id}/cancelar` | Cancela un lote activo |

**Query params `GET /lotes`:**

| Parámetro | Tipo | Descripción |
| --- | --- | --- |
| `estado` | String | `INICIADO`, `PASTEURIZACION`, `FINALIZADO`, etc. |
| `productoCodigo` | String | Filtra por código de producto |
| `desde` | Date | Fecha de inicio |
| `hasta` | Date | Fecha de inicio |
| `soloActivos` | Boolean | Si `true`, excluye FINALIZADO y CANCELADO |

**Body `POST /lotes`:**

```json
{
  "productoCodigo": "QF03",
  "fechaHoraInicio": "2026-05-12T06:00:00",
  "recepcionLecheId": 12
}
```

**Response `POST /lotes`:**

```json
{
  "id": 5,
  "codigoLote": "L13226001",
  "producto": { "codigo": "QF03", "nombre": "Queso Fresco 03" },
  "fechaHoraInicio": "2026-05-12T06:00:00",
  "fechaVencimiento": "2026-06-11",
  "estadoActual": "INICIADO",
  "siguienteEtapa": "PASTEURIZACION"
}
```

**Response `GET /lotes/{id}`** — detalle completo:

```json
{
  "id": 5,
  "codigoLote": "L13226001",
  "producto": { "codigo": "QF03", "nombre": "Queso Fresco 03" },
  "fechaHoraInicio": "2026-05-12T06:00:00",
  "fechaVencimiento": "2026-06-11",
  "estadoActual": "CORTES",
  "siguienteEtapa": "DESUERADO",
  "etapas": [
    {
      "tipoEtapa": "PASTEURIZACION",
      "hora": "06:15:00",
      "temperatura": 72.0,
      "fechaHoraRegistro": "2026-05-12T06:15:00"
    },
    {
      "tipoEtapa": "CLORURO",
      "hora": "06:45:00",
      "temperatura": 35.0,
      "cantidadGramos": 120.0,
      "loteInsumo": "CL-2026-04",
      "fechaHoraRegistro": "2026-05-12T06:45:00"
    }
  ],
  "cortes": [
    { "numeroCorte": 1, "hora": "08:00:00", "observacion": null },
    { "numeroCorte": 2, "hora": "08:20:00", "observacion": "Consistencia adecuada" }
  ],
  "cierre": null
}
```

---

### 3.5 Lotes — Registro de Etapas

Cada endpoint valida que el lote esté en el estado correcto antes de persistir. Al completarse, el lote avanza automáticamente al siguiente estado.

### Pasteurización

| Método | Endpoint |
| --- | --- |
| `POST` | `/lotes/{id}/pasteurizacion` |

```json
{
  "hora": "06:15:00",
  "temperatura": 72.0
}
```

---

### Cloruro

| Método | Endpoint |
| --- | --- |
| `POST` | `/lotes/{id}/cloruro` |

```json
{
  "hora": "06:45:00",
  "temperatura": 35.0,
  "cantidadGramos": 120.0,
  "loteCloruro": "CL-2026-04"
}
```

---

### Cuajo

| Método | Endpoint |
| --- | --- |
| `POST` | `/lotes/{id}/cuajo` |

```json
{
  "hora": "07:10:00",
  "temperatura": 34.5,
  "cantidadGramos": 85.0,
  "loteCuajo": "CJ-2026-11"
}
```

---

### Cortes

Los cortes son acumulativos: se pueden registrar N llamadas al endpoint mientras el lote esté en estado `CORTES`. El lote **no avanza automáticamente** al registrar un corte; el operario debe llamar al endpoint de cierre de cortes para pasar a la siguiente etapa.

| Método | Endpoint | Descripción |
| --- | --- | --- |
| `POST` | `/lotes/{id}/cortes` | Agrega un corte al lote |
| `POST` | `/lotes/{id}/cortes/cerrar` | Cierra la etapa de cortes y avanza al siguiente estado |

**Body `POST /lotes/{id}/cortes`:**

```json
{
  "hora": "08:00:00",
  "observacion": "Consistencia adecuada"
}
```

**Response:**

```json
{
  "numeroCorte": 3,
  "hora": "08:00:00",
  "observacion": "Consistencia adecuada",
  "totalCortesRegistrados": 3
}
```

---

### Lavado / Desuerado *(solo si aplica al producto)*

| Método | Endpoint |
| --- | --- |
| `POST` | `/lotes/{id}/lavado-desuerado` |

```json
{
  "hora": "09:00:00",
  "litros": 30.0
}
```

> Si el producto no requiere esta etapa, el backend retorna `400 ETAPA_NO_APLICA`.
>

---

### Desuerado

| Método | Endpoint |
| --- | --- |
| `POST` | `/lotes/{id}/desuerado` |

```json
{
  "hora": "09:30:00",
  "litros": 45.0
}
```

---

### Salado

| Método | Endpoint |
| --- | --- |
| `POST` | `/lotes/{id}/salado` |

```json
{
  "hora": "10:00:00",
  "temperatura": 15.0,
  "cantidadKg": 2.5,
  "sodioInicial": 18.0,
  "sodioFinal": 22.0,
  "loteSal": "SAL-2026-03"
}
```

---

### Prensado

| Método | Endpoint |
| --- | --- |
| `POST` | `/lotes/{id}/prensado` |

```json
{
  "horaInicio": "10:30:00",
  "horaFin": "12:30:00",
  "presionPsi": 45.0,
  "responsable": "Carlos Martínez"
}
```

> El backend calcula `duracionPrensadoMin = horaFin - horaInicio` y lo persiste.
>

---

### Cierre del Lote

| Método | Endpoint |
| --- | --- |
| `POST` | `/lotes/{id}/cierre` |

```json
{
  "unidadesProducidas": 24,
  "pesoTotalKg": 18.5,
  "observaciones": "Producción sin novedades"
}
```

**Response** — incluye rendimientos calculados:

```json
{
  "codigoLote": "L13226001",
  "estadoActual": "FINALIZADO",
  "fechaHoraCierre": "2026-05-12T13:00:00",
  "unidadesProducidas": 24,
  "pesoTotalKg": 18.5,
  "rendimientoTeorico": 10.2,
  "rendimientoGeneral": 4.1
}
```

> Los cálculos de rendimiento deben confirmarse con el ingeniero. Se dejan como placeholders hasta tener las fórmulas exactas.
>

---

### 3.6 Exportación a Google Sheets

| Método | Endpoint | Descripción |
| --- | --- | --- |
| `POST` | `/exportar/lotes` | Exporta lotes (con filtros) a Sheets |
| `POST` | `/exportar/recepciones` | Exporta recepciones (con filtros) a Sheets |

**Body `POST /exportar/lotes`:**

```json
{
  "desde": "2026-05-01",
  "hasta": "2026-05-12",
  "estado": "FINALIZADO"
}
```

**Response:**

```json
{
  "sheetUrl": "https://docs.google.com/spreadsheets/d/...",
  "filasExportadas": 8
}
```

---