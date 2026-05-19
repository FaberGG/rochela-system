# Reglas de negocio del backend

## Generación del código de loteQueso

```
codigoLote = "L" + diaDeLaAnno(3 dígitos) + ultimosDosDigitosAnno + batchDelDia

batchDelDia = COUNT(loteQuesos donde fecha = hoy) + 1
```

Ejemplo para el primer loteQueso del 12 de mayo de 2026:

```
día 132 del año → "132"
año 2026 → "26"
batch 1 → "1"
resultado → "L132261"
```

## Validación de transiciones de estado

El backend rechaza con `400` cualquier intento de registrar una etapa si el loteQueso no está en el estado previo esperado.

| Estado actual | Acción permitida |
| --- | --- |
| `INICIADO` | Registrar pasteurización (si aplica) o cuajo (si no aplica pasteurización) |
| `PASTEURIZACION` | Registrar cloruro (si aplica) o cuajo (si no aplica cloruro) |
| `CLORURO` | Registrar cuajo |
| `CUAJO` | Avanza automáticamente a `CORTES` |
| `CORTES` | Agregar cortes |
| `CORTES_CERRADOS` | Cerrar etapa de cortes · Registrar lavado/desuerado o desuerado |
| `LAVADO_DESUERADO` | Registrar desuerado |
| `DESUERADO` | Registrar salado |
| `SALADO` | Registrar prensado |
| `PRENSADO` | Registrar cierre |

## Validación de parámetros fisicoquímicos

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

## Cálculo de rendimientos *(pendiente de confirmar fórmulas con el ingeniero)*

```
Rendimiento General  = pesoTotalKg / litrosLeche * 100
Rendimiento Teórico  = [fórmula por confirmar con el ingeniero]
```
