
## 4. Reglas de Negocio del Backend

### 4.1 Generación del código de lote

```
codigoLote = "L" + diaDeLaAnno(3 dígitos) + ultimosDosDigitosAnno + batchDelDia

batchDelDia = COUNT(lotes donde fecha = hoy) + 1
```

Ejemplo para el primer lote del 12 de mayo de 2026:

```
día 132 del año → "132"
año 2026 → "26"
batch 1 → "1"
resultado → "L132261"
```

### 4.2 Validación de transiciones de estado

El backend rechaza con `400` cualquier intento de registrar una etapa si el lote no está en el estado previo esperado.

| Estado actual | Acción permitida |
| --- | --- |
| `INICIADO` | Registrar pasteurización (si aplica) o cuajo (si no aplica pasteurización) |
| `PASTEURIZACION` | Registrar cloruro (si aplica) o cuajo (si no aplica cloruro) |
| `CLORURO` | Registrar cuajo |
| `CUAJO` | Avanza automáticamente a `CORTES` |
| `CORTES` | Agregar cortes · Cerrar etapa de cortes |
| `LAVADO_DESUERADO` | Registrar desuerado |
| `DESUERADO` | Registrar salado |
| `SALADO` | Registrar prensado |
| `PRENSADO` | Registrar cierre |

### 4.3 Validación de parámetros fisicoquímicos

| Parámetro | Mín | Máx |
| --- | --- | --- |
| Densidad | 1.028 | 1.033 |
| pH | 6.6 | 6.8 |
| Acidez Titulable | 0.16 | 0.18 |
| Proteína | 3.50 | — |
| Grasa | 4.0 | — |
| Sólidos Totales | 13.0 | — |
| Agua Añadida | — | 1.0 |
| Punto Crioscópico | 0.53 | 0.55 |

Clasificación resultante:

- **APTA:** todos los parámetros dentro de rango.
- **CONDICIONAL:** uno o más parámetros fuera de rango pero dentro de tolerancia (definir con el cliente).
- **NO_APTA:** uno o más parámetros fuera de rango sin tolerancia.

### 4.4 Cálculo de rendimientos *(pendiente de confirmar fórmulas con el ingeniero)*

```
Rendimiento General  = pesoTotalKg / litrosLeche * 100
Rendimiento Teórico  = [fórmula por confirmar con el ingeniero]
```

---