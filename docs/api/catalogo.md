# API - Catalogo (Productos)

Fuente central: `docs/api/api-docs.json` (Swagger).

## Convenciones

- Base URL: `/api/v1`
- Formato: JSON
- Errores estandar: ver `docs/api/errores.md`

## Endpoints

### GET `/productos`

Retorna todos los productos del catalogo.

**Response 200**

- `application/json`: array de `ProductoResponse`

**Ejemplo response 200**

```json
[
  {
	"id": 1,
	"codigo": "QUESO-001",
	"nombre": "Queso campesino",
	"requierePasteurizacion": true,
	"requiereCloruro": true,
	"requiereLavadoDesuerado": false
  }
]
```

---

### GET `/productos/{codigo}`

Busca un producto por su codigo unico.

**Path params**

| Parametro | Tipo | Descripcion |
| --- | --- | --- |
| `codigo` | string | Codigo del producto |

**Response 200**

- `application/json`: `ProductoResponse`

**Ejemplo response 200**

```json
{
  "id": 1,
  "codigo": "QUESO-001",
  "nombre": "Queso campesino",
  "requierePasteurizacion": true,
  "requiereCloruro": true,
  "requiereLavadoDesuerado": false
}
```

**Response 404**

- `application/json`: `ErrorResponse`

## Esquemas (resumen)

### `ProductoResponse`

- `id` (int64)
- `codigo` (string)
- `nombre` (string)
- `requierePasteurizacion` (boolean)
- `requiereCloruro` (boolean)
- `requiereLavadoDesuerado` (boolean)

