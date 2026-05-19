# Captura requisitos - Procesos

Tipo de Documento: Requisito
Estado: Pendiente

# Documento de Captura de Requisitos

## Sistema de Información — Empresa Productora Láctea

**Versión:** 1.0

**Fase:** Captura Inicial de Requisitos

**Estado:** En revisión

**Fecha:** Mayo 2026

---

## 1. Contexto del Negocio

### 1.1 Descripción General

La empresa es una productora láctea que opera en dos líneas principales:

- **Venta de leche en crudo:** Clientes (generalmente empresas) adquieren la leche y la transportan en carrotanque.
- **Producción y venta de queso:** Se fabrica queso sellado al vacío para venta a empresas y consumidor final.

### 1.2 Áreas de la Empresa

| Área | Descripción |
| --- | --- |
| Administrativa | Gestión general, talento humano, proveedores, área comercial |
| Recepción | Recepción de leche de proveedores, control de calidad inicial |
| Producción | Fabricación de queso |
| Almacenamiento | Almacenamiento de materia prima y producto terminado |

### 1.3 Personal Relevante

- **Ingeniera responsable:** Talento humano, proveedores y área comercial. Usa el software **SIGO** (módulo comercial).
- **Ingeniero de producción y calidad:** Supervisión del proceso productivo y análisis de calidad.

---

## 2. Problema y Objetivo

### 2.1 Situación Actual

Los procesos operativos se gestionan **manualmente o en hojas de cálculo**, lo que dificulta la trazabilidad, genera errores y consume tiempo innecesario.

### 2.2 Objetivo del Proyecto

Desarrollar un **prototipo funcional** de sistema de información que:

1. Convenza al cliente del valor de migrar a un sistema digital.
2. Automatice y facilite la trazabilidad de los procesos clave.
3. Sirva como base para levantar requisitos funcionales y no funcionales completos.

### 2.3 Alcance del Prototipo

El prototipo abarcará dos procesos prioritarios:

1. **Recepción y control de calidad de leche**
2. **Control del proceso de producción de queso**

> Los demás procesos (contacto con proveedores, visitas a fincas, ventas) están fuera del alcance inicial por falta de información suficiente.

---

## 3. Gestión de Proveedores de Leche

### 3.1 Datos del Proveedor

Cada proveedor tiene los siguientes atributos:

| Campo | Descripción |
| --- | --- |
| Nombre empresa / razón social | Nombre bajo el que opera el proveedor |
| Nombre propietario / finca | Nombre del dueño o de la finca |
| Nombre mayordomo | Persona de contacto operativo |
| Teléfono | Contacto telefónico |
| Correo electrónico | Contacto digital |
| Dirección / vereda | Ubicación geográfica |

### 3.2 Proveedores Actuales

```
ANDRÉS RUTA 1
ANDRÉS RUTA 2
EDWIN MAÑANA
EDWIN TARDE
ANGIE MAÑANA
ANGIE TARDE
Calidad Rochela
Calidad Regreso
Caquiona
Valencia
```

---

## 4. Proceso de Recepción de Leche

### 4.1 Descripción

Al recibir la leche de cada proveedor, se registran los parámetros fisicoquímicos para evaluar su calidad y determinar si es apta para producción.

### 4.2 Datos de Recepción

| Campo | Descripción |
| --- | --- |
| Lote de leche (tanque) | Identificador del loteQueso recibido |
| Proveedor | Selección del proveedor correspondiente |
| Ubicación | Tanque 1, Tanque 2, Tanque 3, Cuarto Frío, Proceso |
| Cantidad | Litros recibidos |

### 4.3 Parámetros Fisicoquímicos

Estos valores se miden al momento de la recepción y son analizados por el ingeniero de calidad para verificar que estén dentro de los rangos aceptables.

| Código | Parámetro | Unidad |
| --- | --- | --- |
| G | Grasa | % |
| S | Sólidos | % |
| P | Proteína | % |
| PC | Punto de congelación | °C |
| Tem | Temperatura | °C |
| D | Densidad | g/mL |
| L | Lactosa | % |
| Ts | Sólidos totales | % |
| W | Agua añadida | % |
| PH | Potencial de hidrógeno | — |

### 4.4 Rangos Aceptables de Calidad

La calidad de la leche se clasifica verificando que cada parámetro esté dentro del rango estándar. Las categorías son: **Alta, Estándar, Condicional/Regular y Retrasada** (esta última a definir a futuro).

| Parámetro | Rango / Valor aceptable |
| --- | --- |
| Densidad ajustada | 1,028 – 1,033 g/mL |
| pH | 6,6 – 6,8 |
| Acidez Titulable | 0,16 – 0,18 |
| Tiempo de Reducción Azul de Metileno | > 3 horas |
| Proteína | > 3,50 % |
| Grasa | > 4,0 % |
| Sólidos Totales | > 13 % |
| Agua Añadida | < 1 % |
| Punto Crioscópico | 0,53 – 0,55 °C |

---

## 5. Proceso de Producción de Queso

### 5.1 Catálogo de Productos

La empresa fabrica 5 productos identificados por código:

| Código | Notas |
| --- | --- |
| QF01 | No pasa por pasteurización ni adición de cloruro |
| QF01bs | No pasa por pasteurización ni adición de cloruro |
| SP004 | Se asume que sí pasa por pasteurización y cloruro |
| QF03 | Sí pasa por pasteurización y cloruro |
| QM001 | Se asume que sí pasa por pasteurización y cloruro |

> **Pendiente:** Confirmar con el cliente las etapas exactas para SP004 y QM001, y si existen otras restricciones por producto.

### 5.2 Sistema de Loteado

El código de loteQueso sigue el formato:

```
L  [DDD]  [AA]  [B]

L     → Prefijo fijo
DDD   → Día del año (3 dígitos)
AA    → Últimos dos dígitos del año
B     → Numero de batch (produccion del dia)
```

**Ejemplo:** `L01261` = Día 012, año 2026, batch número 1.

- La **fecha de vencimiento** es siempre 30 días después de la fecha de producción.

### 5.3 Datos Generales del Lote

Al iniciar la producción de un loteQueso se registra:

| Campo | Descripción |
| --- | --- |
| Código de producto | QF01, QF01bs, SP004, QF03, QM001 |
| Fecha y hora de inicio | Marca temporal de inicio del proceso |
| Número de loteQueso calculado | Generado automáticamente según la fórmula |
| Fecha de vencimiento | 30 días después de la fecha de producción |

---

## 6. Etapas del Proceso de Producción

### 6.1 Matriz de Etapas por Producto

| Etapa | QF01 | QF01bs | SP004 | QF03 | QM001 |
| --- | --- | --- | --- | --- | --- |
| Pasteurización | ❌ | ❌ | ✅ (asumido) | ✅ | ✅ (asumido) |
| Adición de Cloruro | ❌ | ❌ | ✅ (asumido) | ✅ | ✅ (asumido) |
| Cuajo | ✅ | ✅ | ✅ | ✅ | ✅ |
| Cortes | ✅ | ✅ | ✅ | ✅ | ✅ |
| Lavado / Desuerado | ⚠️ Opcional | ⚠️ Opcional | ⚠️ Opcional | ⚠️ Opcional | ⚠️ Opcional |
| Desuerado | ✅ | ✅ | ✅ | ✅ | ✅ |
| Salado | ✅ | ✅ | ✅ | ✅ | ✅ |
| Prensado | ✅ | ✅ | ✅ | ✅ | ✅ |

> ⚠️ El **Lavado/Desuerado** no aplica a todos los productos. Requiere confirmación por producto con el cliente.

---

### 6.2 Etapa: Pasteurización

La leche se transfiere a la marmita y se calienta con vapor de caldera.

| Campo | Tipo |
| --- | --- |
| Hora | Timestamp |
| Temperatura | °C |

---

### 6.3 Etapa: Adición de Cloruro

| Campo | Tipo |
| --- | --- |
| Hora | Timestamp |
| Temperatura | °C |
| Cantidad | Gramos |
| Lote del cloruro | Texto |

---

### 6.4 Etapa: Cuajo

| Campo | Tipo |
| --- | --- |
| Hora | Timestamp |
| Temperatura | °C |
| Cantidad | Gramos |
| Lote del cuajo | Texto |

---

### 6.5 Etapa: Cortes

Se pueden realizar múltiples cortes (cantidad variable según criterio del jefe de producción).

| Campo | Tipo |
| --- | --- |
| Hora del corte (por cada corte) | Timestamp |
| Observación (opcional) | Texto libre |

---

### 6.6 Etapa: Lavado / Desuerado

> Esta etapa es **opcional** y no aplica a todos los productos.

| Campo | Tipo |
| --- | --- |
| Hora | Timestamp |
| Litros de lavado/desuerado | Numérico |

---

### 6.7 Etapa: Desuerado

> Esta etapa es **diferente** al Lavado/Desuerado anterior.

| Campo | Tipo |
| --- | --- |
| Hora | Timestamp |
| Litros | Numérico |

---

### 6.8 Etapa: Salado

| Campo | Tipo |
| --- | --- |
| Hora | Timestamp |
| Temperatura | °C |
| Cantidad de sal | Kilogramos |
| Nivel de sodio inicial | % |
| Nivel de sodio final | % |
| Lote del bulto de sal | Texto |

---

### 6.9 Etapa: Prensado

| Campo | Tipo |
| --- | --- |
| Hora de inicio | Timestamp |
| Hora de fin | Timestamp |
| Duración calculada | Fin − Inicio |
| Presión | PSI |
| Responsable | Texto |

---

### 6.10 Cierre del Lote

Al finalizar la producción:

| Campo | Tipo |
| --- | --- |
| Unidades producidas | Entero |
| Peso total producido | Kilogramos |
| Rendimiento teórico | Calculado |
| Rendimiento general | Calculado |
| Proveedor de la leche | Referencia al proveedor |
| Observaciones adicionales | Texto libre |

> El ingeniero de producción y calidad calcula los índices de rendimiento y genera reportes a partir de estos datos.

---

## 7. Requisitos Identificados

### 7.1 Requisitos Funcionales (Preliminares)

**Módulo de Recepción de Leche:**

- RF-01: Registrar la recepción de leche por proveedor con sus parámetros fisicoquímicos.
- RF-02: Validar automáticamente si los parámetros están dentro de los rangos aceptables.
- RF-03: Clasificar la calidad de la leche (Alta, Estándar, Condicional/Regular).
- RF-04: Notificar o enviar el registro al ingeniero de calidad para revisión.

**Módulo de Producción de Queso:**

- RF-05: Generar automáticamente el código de loteQueso según la fórmula definida.
- RF-06: Calcular automáticamente la fecha de vencimiento (fecha de producción + 30 días).
- RF-07: Registrar cada etapa del proceso con sus variables correspondientes.
- RF-08: Restringir las etapas disponibles según el código de producto seleccionado.
- RF-09: Permitir un número variable de cortes con observaciones opcionales.
- RF-10: Marcar la etapa de Lavado/Desuerado como opcional y configurable por producto.
- RF-11: Calcular automáticamente la duración del prensado.
- RF-12: Registrar unidades producidas, peso total y calcular rendimientos.
- RF-13: Generar reportes de producción por loteQueso.

**Gestión de Proveedores:**

- RF-14: CRUD de proveedores con todos sus atributos.

### 7.2 Requisitos No Funcionales (Preliminares)

- RNF-01: La interfaz debe ser usable por operarios sin formación técnica avanzada.
- RNF-02: El sistema debe poder funcionar desde distintas áreas de la planta.
- RNF-03: Los datos deben tener trazabilidad completa por loteQueso.
- RNF-04: El sistema debe integrarse o coexistir con el software SIGO existente (o al menos no duplicar módulos innecesariamente).

---

## 8. Preguntas Abiertas y Pendientes

| # | Pregunta | Prioridad |
| --- | --- | --- |
| 1 | ¿Cuáles etapas aplican exactamente para SP004 y QM001? | Alta |
| 2 | ¿Qué productos pasan por la etapa de Lavado/Desuerado? | Alta |
| 3 | ¿Cómo se calcula el rendimiento teórico y el rendimiento general? | Alta |
| 4 | ¿Cómo funciona exactamente la clasificación Alta / Estándar / Condicional / Retrasada? | Alta |
| 5 | ¿El sistema debe gestionar usuarios y roles (ej. operario vs. ingeniero)? | Media |
| 6 | ¿Cómo es el proceso de ventas y despacho? (sin información suficiente aún) | Media |
| 7 | ¿Cómo son las visitas a fincas y el contacto con proveedores? | Baja |
| 8 | ¿Qué reportes específicos necesita generar el ingeniero de calidad? | Alta |
| 9 | ¿El sistema debe integrarse directamente con SIGO? | Media |

---

## 9. Glosario

| Término | Definición |
| --- | --- |
| Batch / Lote | Unidad identificable de producción bajo las mismas condiciones y proceso |
| Marmita | Recipiente donde se procesa la leche durante la pasteurización |
| Carrotanque | Vehículo cisterna usado para transportar la leche en crudo |
| Desuerado | Proceso de separación del suero del queso |
| Lavado/Desuerado | Etapa previa al desuerado, no equivalente a este; no aplica a todos los productos |
| Punto crioscópico | Temperatura a la que congela la leche; indica adulteración si está fuera de rango |
| Rendimiento | Relación entre la cantidad de leche recibida y el queso producido |
| SIGO | Software actualmente en uso por el área administrativa y comercial |

