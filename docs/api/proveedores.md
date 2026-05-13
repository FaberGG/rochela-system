# API - Proveedores

Fuente central: `docs/api/api-docs.json` (Swagger).

## Convenciones

- Base URL: `/api/v1`
- Formato: JSON
- Errores estandar: ver `docs/api/errores.md`

## Endpoints

### GET `/proveedores`

Lista proveedores activos.

**Response 200**

- `application/json`: array de `ProveedorResponse`

**Ejemplo response 200**

```json
[
  {
    "id": 10,
    "nombreEmpresa": "Lacteos del Norte",
    "nombrePropietario": "Juan Perez",
    "nombreMayordomo": "Carlos Gomez",
    "telefono": "+57 3001234567",
    "correo": "contacto@lacteos.com",
    "direccion": "Km 5 via principal, Vereda La Isla",
    "activo": true
  }
]
```

---

### POST `/proveedores`

Crea un nuevo proveedor.

**Request body**

- `application/json`: `ProveedorRequest`

**Ejemplo request**

```json
{
  "nombreEmpresa": "Lacteos del Norte",
  "nombrePropietario": "Juan Perez",
  "nombreMayordomo": "Carlos Gomez",
  "telefono": "+57 3001234567",
  "correo": "contacto@lacteos.com",
  "direccion": "Km 5 via principal, Vereda La Isla",
  "activo": true
}
```

**Response 201**

- `application/json`: `ProveedorResponse`

**Ejemplo response 201**

```json
{
  "id": 10,
  "nombreEmpresa": "Lacteos del Norte",
  "nombrePropietario": "Juan Perez",
  "nombreMayordomo": "Carlos Gomez",
  "telefono": "+57 3001234567",
  "correo": "contacto@lacteos.com",
  "direccion": "Km 5 via principal, Vereda La Isla",
  "activo": true
}
```

---

### GET `/proveedores/{id}`

Busca un proveedor por id.

**Path params**

| Parametro | Tipo | Descripcion |
| --- | --- | --- |
| `id` | integer (int64) | Id del proveedor |

**Response 200**

- `application/json`: `ProveedorResponse`

**Ejemplo response 200**

```json
{
  "id": 10,
  "nombreEmpresa": "Lacteos del Norte",
  "nombrePropietario": "Juan Perez",
  "nombreMayordomo": "Carlos Gomez",
  "telefono": "+57 3001234567",
  "correo": "contacto@lacteos.com",
  "direccion": "Km 5 via principal, Vereda La Isla",
  "activo": true
}
```

**Response 404**

- `application/json`: `ErrorResponse`

---

### PUT `/proveedores/{id}`

Actualiza los datos de un proveedor.

**Path params**

| Parametro | Tipo | Descripcion |
| --- | --- | --- |
| `id` | integer (int64) | Id del proveedor |

**Request body**

- `application/json`: `ProveedorRequest`

**Ejemplo request**

```json
{
  "nombreEmpresa": "Lacteos del Norte",
  "nombrePropietario": "Juan Perez",
  "nombreMayordomo": "Carlos Gomez",
  "telefono": "+57 3001234567",
  "correo": "contacto@lacteos.com",
  "direccion": "Km 5 via principal, Vereda La Isla",
  "activo": true
}
```

**Response 200**

- `application/json`: `ProveedorResponse`

**Ejemplo response 200**

```json
{
  "id": 10,
  "nombreEmpresa": "Lacteos del Norte",
  "nombrePropietario": "Juan Perez",
  "nombreMayordomo": "Carlos Gomez",
  "telefono": "+57 3001234567",
  "correo": "contacto@lacteos.com",
  "direccion": "Km 5 via principal, Vereda La Isla",
  "activo": true
}
```

**Response 404**

- `application/json`: `ErrorResponse`

---

### DELETE `/proveedores/{id}`

Marca un proveedor como inactivo.

**Path params**

| Parametro | Tipo | Descripcion |
| --- | --- | --- |
| `id` | integer (int64) | Id del proveedor |

**Response 204**

- Sin contenido.

**Response 404**

- `application/json`: `ErrorResponse`

## Esquemas (resumen)

### `ProveedorRequest`

- `nombreEmpresa` (string)
- `nombrePropietario` (string)
- `nombreMayordomo` (string)
- `telefono` (string)
- `correo` (string)
- `direccion` (string)
- `activo` (boolean)

### `ProveedorResponse`

- `id` (int64)
- `nombreEmpresa` (string)
- `nombrePropietario` (string)
- `nombreMayordomo` (string)
- `telefono` (string)
- `correo` (string)
- `direccion` (string)
- `activo` (boolean)

