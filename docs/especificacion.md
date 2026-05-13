# Especificacion Backend

Tipo de Documento: Especificación
Estado: Pendiente
Fecha de Creación: May 12, 2026

# Especificación de Backend — La rochela system

## Sistema de Seguimiento de Producción Láctea

**Versión:** 1.0

**Stack:** Spring Boot 3.x · Java 17 · H2 (prototipo) · Google Sheets API

**Estado:** Especificación inicial

**Fecha:** Mayo 2026

---

## 1. Usuarios y Flujos

El prototipo considera dos perfiles de usuario. No se implementa autenticación en esta fase; los perfiles se mencionan para delimitar responsabilidades y orientar el diseño de pantallas en el frontend.

### 1.1 Operario de Producción

Es quien ejecuta físicamente el proceso en planta y registra los datos en el sistema.

**Flujo principal — Seguimiento de lote:**

```
1. Ve la lista de lotes activos con su estado actual
2. Selecciona un lote
3. Ve el detalle del lote: datos generales, historial de etapas registradas
4. Registra los datos de la etapa en curso
5. El sistema avanza el lote al siguiente estado
6. Puede agregar cortes adicionales mientras el lote está en estado CORTES
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

---

### 1.2 Ingeniero de Producción y Calidad

Supervisa los procesos, analiza la información registrada y genera reportes.

**Flujo principal — Consulta de lotes:**

```
1. Ve todos los lotes: activos, finalizados y cancelados
2. Puede filtrar por fecha, producto o estado
3. Selecciona un lote y ve el detalle completo con todas las etapas
4. Consulta los índices de rendimiento del lote finalizado
5. Exporta los datos a Google Sheets
```

**Flujo secundario — Revisión de recepciones de leche:**

```
1. Ve la lista de recepciones registradas
2. Selecciona una recepción y ve los parámetros con indicadores de rango
3. Exporta el historial de recepciones a Google Sheets
```

---

## 2. Modelo de Entidades

### 2.1 Diagrama de relaciones

[https://dbdiagram.io/d/6a039d747a923b947295d0a6](https://dbdiagram.io/d/6a039d747a923b947295d0a6)

```
// Sistema de Seguimiento de Producción Láctea
// Docs: https://dbml.dbdiagram.io/docs

Table producto {
  id bigint [primary key, increment]
  codigo varchar [unique, not null, note: 'QF01, QF01bs, SP004, QF03, QM001']
  nombre varchar [not null]
  requiere_pasteurizacion boolean [not null, default: true]
  requiere_cloruro boolean [not null, default: true]
  requiere_lavado_desuerado boolean [not null, default: false]
}

Table proveedor {
  id bigint [primary key, increment]
  nombre_empresa varchar [not null]
  nombre_propietario varchar
  nombre_mayordomo varchar
  telefono varchar
  correo varchar
  direccion varchar
  activo boolean [not null, default: true]
}

Table recepcion_leche {
  id bigint [primary key, increment]
  fecha_hora timestamp [not null]
  proveedor_id bigint [not null]
  lote_tanque varchar [not null]
  ubicacion varchar [not null, note: 'TANQUE_1, TANQUE_2, TANQUE_3, CUARTO_FRIO, PROCESO']
  cantidad_litros double [not null]
  grasa double
  solidos double
  proteina double
  punto_congelacion double
  temperatura double
  densidad double
  lactosa double
  solidos_totales double
  agua_anadida double
  ph double
  resultado_validacion varchar [not null, note: 'APTA, NO_APTA, CONDICIONAL']
  observaciones text
}

Table lote {
  id bigint [primary key, increment]
  codigo_lote varchar [unique, not null, note: 'Formato: L[DDD][AA][B]']
  producto_id bigint [not null]
  recepcion_leche_id bigint [note: 'Nullable — leche asociada al lote']
  fecha_hora_inicio timestamp [not null]
  fecha_vencimiento date [not null, note: 'fecha_inicio + 30 días']
  estado_actual varchar [not null, note: 'INICIADO, PASTEURIZACION, CLORURO, CUAJO, CORTES, LAVADO_DESUERADO, DESUERADO, SALADO, PRENSADO, FINALIZADO, CANCELADO']
  batch_del_dia integer [not null]
  observaciones text
}

Table cierre_lote {
  id bigint [primary key, increment]
  lote_id bigint [unique, not null, note: 'Un cierre por lote']
  fecha_hora_cierre timestamp [not null]
  unidades_producidas integer [not null]
  peso_total_kg double [not null]
  rendimiento_teorico double
  rendimiento_general double
}

// ─── Herencia JOINED: tabla base de etapas ───────────────────────────────────

Table etapa_registro {
  id bigint [primary key, increment]
  lote_id bigint [not null]
  tipo_etapa varchar [not null, note: 'Discriminador: PASTEURIZACION, CLORURO, CUAJO, LAVADO_DESUERADO, DESUERADO, SALADO, PRENSADO']
  hora time [not null, note: 'Hora operativa de inicio de la etapa']
  fecha_hora_registro timestamp [not null]
}

// ─── Tablas hijas (PK = FK hacia etapa_registro) ─────────────────────────────

Table etapa_pasteurizacion {
  id bigint [primary key, note: 'FK → etapa_registro.id']
  temperatura double [not null, note: '°C']
}

Table etapa_cloruro {
  id bigint [primary key, note: 'FK → etapa_registro.id']
  temperatura double [not null, note: '°C']
  cantidad_gramos double [not null]
  lote_cloruro varchar [not null]
}

Table etapa_cuajo {
  id bigint [primary key, note: 'FK → etapa_registro.id']
  temperatura double [not null, note: '°C']
  cantidad_gramos double [not null]
  lote_cuajo varchar [not null]
}

Table etapa_lavado_desuerado {
  id bigint [primary key, note: 'FK → etapa_registro.id']
  litros double [not null]
}

Table etapa_desuerado {
  id bigint [primary key, note: 'FK → etapa_registro.id']
  litros double [not null]
}

Table etapa_salado {
  id bigint [primary key, note: 'FK → etapa_registro.id']
  temperatura double [not null, note: '°C']
  cantidad_kg double [not null]
  sodio_inicial double [not null, note: '%']
  sodio_final double [not null, note: '%']
  lote_sal varchar [not null]
}

Table etapa_prensado {
  id bigint [primary key, note: 'FK → etapa_registro.id']
  hora_fin time [not null]
  duracion_minutos integer [not null, note: 'Calculado: hora_fin − hora (base)']
  presion_psi double [not null]
  responsable varchar [not null]
}

Table corte {
  id bigint [primary key, increment]
  lote_id bigint [not null]
  numero_corte integer [not null]
  hora time [not null]
  observacion text
  fecha_hora_registro timestamp [not null]
}

// ─── Referencias ─────────────────────────────────────────────────────────────

Ref: recepcion_leche.proveedor_id > proveedor.id
Ref: lote.producto_id > producto.id
Ref: lote.recepcion_leche_id > recepcion_leche.id
Ref: cierre_lote.lote_id - lote.id
Ref: etapa_registro.lote_id > lote.id
Ref: corte.lote_id > lote.id

// Herencia JOINED: cada tabla hija comparte PK con la base
Ref: etapa_pasteurizacion.id - etapa_registro.id
Ref: etapa_cloruro.id - etapa_registro.id
Ref: etapa_cuajo.id - etapa_registro.id
Ref: etapa_lavado_desuerado.id - etapa_registro.id
Ref: etapa_desuerado.id - etapa_registro.id
Ref: etapa_salado.id - etapa_registro.id
Ref: etapa_prensado.id - etapa_registro.id

// ─── Datos iniciales ──────────────────────────────────────────────────────────

Records producto(id, codigo, nombre, requiere_pasteurizacion, requiere_cloruro, requiere_lavado_desuerado) {
  1, 'QF01',   'Queso Fresco 01',        false, false, false
  2, 'QF01bs', 'Queso Fresco 01 BS',     false, false, false
  3, 'SP004',  'SP 004',                 true,  true,  false
  4, 'QF03',   'Queso Fresco 03',        true,  true,  false
  5, 'QM001',  'Queso Maduro 001',       true,  true,  false
}
```

**Estrategia de herencia:** `@Inheritance(InheritanceType.JOINED)` en JPA. La tabla `etapa_registro` contiene los campos comunes a todas las etapas. Cada tabla hija contiene únicamente los campos propios de esa etapa y comparte la PK con la tabla base mediante FK. No existen campos `null` por diseño: cada fila de una tabla hija siempre tendrá todos sus campos significativos.

---

### 2.2 Entidades

### `Producto`

Catálogo de los 5 productos fabricados. Define qué etapas aplican a cada uno.

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK autogenerada |
| `codigo` | String | Único. QF01, QF01bs, SP004, QF03, QM001 |
| `nombre` | String | Nombre descriptivo del producto |
| `requierePasteurizacion` | Boolean | Si el producto pasa por pasteurización |
| `requiereCloruro` | Boolean | Si el producto pasa por adición de cloruro |
| `requiereLavadoDesuerado` | Boolean | Si el producto pasa por lavado/desuerado |

**Datos iniciales (seed):**

| Código | Pasteurización | Cloruro | Lavado/Desuerado |
| --- | --- | --- | --- |
| QF01 | ❌ | ❌ | ⚠️ por confirmar |
| QF01bs | ❌ | ❌ | ⚠️ por confirmar |
| SP004 | ✅ | ✅ | ⚠️ por confirmar |
| QF03 | ✅ | ✅ | ⚠️ por confirmar |
| QM001 | ✅ | ✅ | ⚠️ por confirmar |

---

### `Proveedor`

Catálogo de proveedores de leche.

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK autogenerada |
| `nombreEmpresa` | String | Nombre de la empresa o ruta |
| `nombrePropietario` | String | Nombre del propietario o finca |
| `nombreMayordomo` | String | Persona de contacto operativo |
| `telefono` | String |  |
| `correo` | String |  |
| `direccion` | String | Dirección o vereda |
| `activo` | Boolean | Soft delete |

---

### `RecepcionLeche`

Registro de cada recepción de leche de un proveedor.

La entidad `recepcion_leche` queda así actualizada:

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK |
| `fecha` | LocalDate | Fecha de la recepción |
| `fechaHora` | LocalDateTime | Timestamp completo del registro |
| `proveedorId` | FK → Proveedor |  |
| `jornada` | Enum | AM, PM |
| `ubicacion` | Enum | TANQUE_1, TANQUE_2, TANQUE_3, CUARTO_FRIO, PROCESO |
| `cantidadLitros` | Double |  |
| `realizadoPor` | String | Nombre del funcionario |
| `analisisOrganoléptico` | Enum | CUMPLE, NO_CUMPLE |
| `colorCumple` | Boolean | Resultado de color |
| `olorCumple` | Boolean | Resultado de olor |
| `alcohol` | Enum | POSITIVO, NEGATIVO |
| `temperatura` | Double | °C |
| `densidad` | Double | g/mL |
| `ph` | Double |  |
| `proteina` | Double | % |
| `grasa` | Double | % |
| `solidosTotales` | Double | % |
| `acidezTitulable` | Double |  |
| `aguaAnadida` | Double | % |
| `puntoCrioscopico` | Double | °C |
| `horaInicioReductasa` | LocalTime | Hora inicio prueba azul de metileno |
| `horaFinReductasa` | LocalTime | Nullable — se llena en paso 2 |
| `tiempoReductasaMinutos` | Integer | Calculado al cerrar: horaFin − horaInicio |
| `resultadoValidacion` | Enum | APTA, NO_APTA, CONDICIONAL |
| `estadoRecepcion` | Enum | PENDIENTE_REDUCTASA, COMPLETA |
| `observaciones` | String |  |

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK autogenerada |
| `fechaHora` | LocalDateTime | Momento de la recepción |
| `proveedorId` | FK → Proveedor |  |
| `loteTanque` | String | Identificador del tanque |
| `ubicacion` | Enum | TANQUE_1, TANQUE_2, TANQUE_3, CUARTO_FRIO, PROCESO |
| `cantidadLitros` | Double |  |
| `grasa` | Double | % |
| `solidos` | Double | % |
| `proteina` | Double | % |
| `puntoCongelacion` | Double | °C |
| `temperatura` | Double | °C |
| `densidad` | Double | g/mL |
| `lactosa` | Double | % |
| `solidosTotales` | Double | % |
| `aguaAnadida` | Double | % |
| `ph` | Double |  |
| `resultadoValidacion` | Enum | APTA, NO_APTA, CONDICIONAL |
| `observaciones` | String | Texto libre |

> La validación de rangos se ejecuta en el backend al momento de crear el registro y se persiste en `resultadoValidacion`.
> 

---

### `Lote`

Representa una producción de queso desde el inicio hasta el cierre. Solo contiene datos de apertura y estado. Los datos de cierre se separan en `CierreLote` para mantener la responsabilidad única de la entidad.

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK autogenerada |
| `codigoLote` | String | Único. Generado: L[DDD][AA][B] |
| `productoId` | FK → Producto |  |
| `recepcionLecheId` | FK → RecepcionLeche | Nullable — leche asociada al lote |
| `fechaHoraInicio` | LocalDateTime |  |
| `fechaVencimiento` | LocalDate | fechaInicio + 30 días |
| `estadoActual` | Enum | Ver sección 2.3 |
| `batchDelDia` | Integer | Número de batch usado en la generación del código |
| `observaciones` | String | Texto libre |

---

### `CierreLote`

Datos registrados al finalizar la producción de un lote. Relación 1:1 con `Lote`, creada únicamente cuando el lote llega al estado `FINALIZADO`.

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK autogenerada |
| `loteId` | FK → Lote | Unique — un cierre por lote |
| `fechaHoraCierre` | LocalDateTime |  |
| `unidadesProducidas` | Integer |  |
| `pesoTotalKg` | Double |  |
| `rendimientoTeorico` | Double | Calculado por el backend |
| `rendimientoGeneral` | Double | Calculado por el backend |

---

### `EtapaRegistro` *(tabla base — no se instancia directamente)*

Campos comunes a todas las etapas del proceso. Cada etapa concreta extiende esta entidad con sus propios campos en una tabla hija separada.

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK autogenerada. Compartida con la tabla hija vía FK |
| `loteId` | FK → Lote |  |
| `tipoEtapa` | Enum | Discriminador: PASTEURIZACION, CLORURO, CUAJO, LAVADO_DESUERADO, DESUERADO, SALADO, PRENSADO |
| `hora` | LocalTime | Hora operativa de inicio de la etapa |
| `fechaHoraRegistro` | LocalDateTime | Momento exacto en que se guardó el registro |

---

### `EtapaPasteurizacion` *(tabla hija)*

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | FK → EtapaRegistro | PK compartida |
| `temperatura` | Double | °C al momento de la pasteurización |

---

### `EtapaCloruro` *(tabla hija)*

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | FK → EtapaRegistro | PK compartida |
| `temperatura` | Double | °C |
| `cantidadGramos` | Double | Cantidad de cloruro agregada |
| `loteCloruro` | String | Lote del insumo |

---

### `EtapaCuajo` *(tabla hija)*

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | FK → EtapaRegistro | PK compartida |
| `temperatura` | Double | °C |
| `cantidadGramos` | Double | Cantidad de cuajo agregada |
| `loteCuajo` | String | Lote del insumo |

---

### `EtapaLavadoDesuerado` *(tabla hija)*

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | FK → EtapaRegistro | PK compartida |
| `litros` | Double | Litros del lavado/desuerado |

---

### `EtapaDesuerado` *(tabla hija)*

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | FK → EtapaRegistro | PK compartida |
| `litros` | Double | Litros del desuerado |

> El desuerado y el lavado/desuerado son etapas distintas y no equivalentes, aunque compartan el campo `litros`.
> 

---

### `EtapaSalado` *(tabla hija)*

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | FK → EtapaRegistro | PK compartida |
| `temperatura` | Double | °C |
| `cantidadKg` | Double | Kilogramos de sal |
| `sodioInicial` | Double | % de sodio al inicio |
| `sodioFinal` | Double | % de sodio al final |
| `loteSal` | String | Lote del bulto de sal |

---

### `EtapaPrensado` *(tabla hija)*

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | FK → EtapaRegistro | PK compartida |
| `horaFin` | LocalTime | Hora de salida del prensado |
| `duracionMinutos` | Integer | Calculado: horaFin − hora (base) |
| `presionPsi` | Double | Presión aplicada |
| `responsable` | String | Nombre del responsable |

---

### `Corte`

Cada corte del queso se registra como una fila independiente. Un lote puede tener N cortes mientras esté en estado `CORTES`.

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK autogenerada |
| `loteId` | FK → Lote |  |
| `numeroCorte` | Integer | Orden del corte: 1, 2, 3... |
| `hora` | LocalTime | Hora del corte |
| `observacion` | String | Nullable |
| `fechaHoraRegistro` | LocalDateTime |  |

---

### 2.3 Tablas físicas resultantes

Esta es la representación de cómo queda la base de datos después de aplicar la herencia `JOINED`. Sirve como referencia para migraciones o para verificar el esquema generado por Hibernate.

```
producto
proveedor
recepcion_leche
lote
cierre_lote
etapa_registro          ← base: id, lote_id, tipo_etapa, hora, fecha_hora_registro
etapa_pasteurizacion    ← id (FK), temperatura
etapa_cloruro           ← id (FK), temperatura, cantidad_gramos, lote_cloruro
etapa_cuajo             ← id (FK), temperatura, cantidad_gramos, lote_cuajo
etapa_lavado_desuerado  ← id (FK), litros
etapa_desuerado         ← id (FK), litros
etapa_salado            ← id (FK), temperatura, cantidad_kg, sodio_inicial, sodio_final, lote_sal
etapa_prensado          ← id (FK), hora_fin, duracion_minutos, presion_psi, responsable
corte
```

Cuando JPA necesita reconstruir una `EtapaPrensado` completa, hace un `JOIN` entre `etapa_registro` y `etapa_prensado` por `id`. Esto es transparente para el código Java.

---

### 2.3 Estados del Lote (`EstadoLote`)

El backend es el responsable de calcular y hacer cumplir las transiciones de estado. El frontend nunca define el siguiente estado: solo registra los datos de la etapa actual y el backend avanza el estado.

![image.png](../../../../bb57c93a-7dd9-4351-8fd8-720410985a8d_ExportBlock-d73d2eef-9d97-4d10-80c9-7dd5012d10c8/ExportBlock-d73d2eef-9d97-4d10-80c9-7dd5012d10c8-Part-1/image.png)

```
@startuml

skinparam state {
  BackgroundColor #F1EFE8
  BorderColor #5F5E5A
  FontColor #2C2C2A
  ArrowColor #5F5E5A
  StartColor #2C2C2A
  EndColor #2C2C2A
}

skinparam state<<activo>> {
  BackgroundColor #E1F5EE
  BorderColor #0F6E56
  FontColor #085041
}

skinparam state<<terminal_ok>> {
  BackgroundColor #EEEDFE
  BorderColor #534AB7
  FontColor #26215C
}

skinparam state<<terminal_err>> {
  BackgroundColor #FCEBEB
  BorderColor #A32D2D
  FontColor #501313
}

[*] --> INICIADO

state INICIADO <<activo>>
INICIADO : Código de lote generado
INICIADO : Fecha de vencimiento calculada

state PASTEURIZACION <<activo>>
PASTEURIZACION : hora · temperatura

state CLORURO <<activo>>
CLORURO : hora · temperatura
CLORURO : cantidad (g) · lote insumo

state CUAJO <<activo>>
CUAJO : hora · temperatura
CUAJO : cantidad (g) · lote insumo

state LAVADO_DESUERADO <<activo>>
LAVADO_DESUERADO : hora · litros

state DESUERADO <<activo>>
DESUERADO : hora · litros

state SALADO <<activo>>
SALADO : hora · temperatura · cantidad (kg)
SALADO : sodio inicial % · sodio final % · lote sal

state PRENSADO <<activo>>
PRENSADO : hora inicio · hora fin
PRENSADO : duración calculada · presión (psi) · responsable

state FINALIZADO <<terminal_ok>>
FINALIZADO : unidades · peso total (kg)
FINALIZADO : rendimiento teórico · rendimiento general

state CANCELADO <<terminal_err>>

state CORTES <<activo>> {
  [*] --> RegistrandoCortes
  RegistrandoCortes --> RegistrandoCortes : + corte (hora · observación)
  RegistrandoCortes --> [*] : cerrar etapa
}

INICIADO --> PASTEURIZACION  : [requiere pasteurización]
INICIADO --> CLORURO         : [no requiere pasteurización\ny requiere cloruro]
INICIADO --> CUAJO           : [no requiere pasteurización\nni cloruro]

PASTEURIZACION --> CLORURO   : [requiere cloruro]
PASTEURIZACION --> CUAJO     : [no requiere cloruro]

CLORURO --> CUAJO

CUAJO --> CORTES

CORTES --> LAVADO_DESUERADO  : [requiere lavado/desuerado]
CORTES --> DESUERADO         : [no requiere lavado/desuerado]

LAVADO_DESUERADO --> DESUERADO

DESUERADO --> SALADO

SALADO --> PRENSADO

PRENSADO --> FINALIZADO

FINALIZADO --> [*]

INICIADO         --> CANCELADO
PASTEURIZACION   --> CANCELADO
CLORURO          --> CANCELADO
CUAJO            --> CANCELADO
CORTES           --> CANCELADO
LAVADO_DESUERADO --> CANCELADO
DESUERADO        --> CANCELADO
SALADO           --> CANCELADO
PRENSADO         --> CANCELADO

CANCELADO --> [*]

@enduml
```

**Regla de transición:** El backend expone un endpoint por etapa. Al recibir los datos de una etapa, valida que el lote esté en el estado correcto antes de persistir el registro y actualizar `estadoActual`.

---

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

## 5. Estructura del Proyecto Spring Boot

```
src/
└── main/
    ├── java/com/rochela/rochelasystem
    │   │
    │   ├── shared/                                    # Código transversal a todos los módulos
    │   │   ├── config/
    │   │   │   ├── DataInitializer.java               # Seed de productos y proveedores
    │   │   │   └── GoogleSheetsConfig.java            # Credenciales y cliente de Sheets API
    │   │   ├── exception/
    │   │   │   ├── EstadoInvalidoException.java
    │   │   │   ├── EtapaNoAplicaException.java
    │   │   │   └── GlobalExceptionHandler.java
    │   │   └── enums/
    │   │       ├── EstadoLote.java
    │   │       ├── TipoEtapa.java
    │   │       ├── UbicacionTanque.java
    │   │       └── ResultadoValidacion.java
    │   │
    │   └── modulos/
    │       │
    │       ├── proveedores/                           # Módulo: Proveedores
    │       │   ├── controller/
    │       │   │   └── ProveedorController.java
    │       │   ├── service/
    │       │   │   └── ProveedorService.java
    │       │   ├── repository/
    │       │   │   └── ProveedorRepository.java
    │       │   ├── model/
    │       │   │   └── Proveedor.java
    │       │   └── dto/
    │       │       ├── ProveedorRequestDto.java
    │       │       └── ProveedorResponseDto.java
    │       │
    │       ├── catalogo/                              # Módulo: Catálogo de productos
    │       │   ├── controller/
    │       │   │   └── ProductoController.java
    │       │   ├── service/
    │       │   │   └── ProductoService.java
    │       │   ├── repository/
    │       │   │   └── ProductoRepository.java
    │       │   ├── model/
    │       │   │   └── Producto.java
    │       │   └── dto/
    │       │       └── ProductoResponseDto.java
    │       │
    │       ├── recepcion/                             # Módulo: Recepción de leche
    │       │   ├── controller/
    │       │   │   └── RecepcionLecheController.java
    │       │   ├── service/
    │       │   │   ├── RecepcionService.java          # Orquestación del flujo de recepción
    │       │   │   └── ValidacionFisicoquimicaService.java  # Validación de rangos por parámetro
    │       │   ├── repository/
    │       │   │   └── RecepcionLecheRepository.java
    │       │   ├── model/
    │       │   │   └── RecepcionLeche.java
    │       │   └── dto/
    │       │       ├── RecepcionRequestDto.java
    │       │       ├── RecepcionResponseDto.java
    │       │       ├── ReductasaCierreDto.java        # PATCH hora fin reductasa
    │       │       └── ValidacionDetalleDto.java      # Resultado por parámetro
    │       │
    │       ├── produccion/                            # Módulo: Producción de queso
    │       │   ├── controller/
    │       │   │   ├── LoteController.java
    │       │   │   └── EtapaController.java           # Endpoints de registro por etapa
    │       │   ├── service/
    │       │   │   ├── LoteService.java               # Orquestación y transición de estados
    │       │   │   ├── LoteadoService.java            # Generación del código de lote
    │       │   │   ├── EtapaService.java              # Persistencia de etapas y validación de transición
    │       │   │   └── RendimientoService.java        # Cálculo de índices al cierre
    │       │   ├── repository/
    │       │   │   ├── LoteRepository.java
    │       │   │   ├── CierreLoteRepository.java
    │       │   │   ├── EtapaRegistroRepository.java   # Repositorio base (consultas polimórficas)
    │       │   │   └── CorteRepository.java
    │       │   ├── model/
    │       │   │   ├── Lote.java
    │       │   │   ├── CierreLote.java
    │       │   │   ├── Corte.java
    │       │   │   └── etapa/
    │       │   │       ├── EtapaRegistro.java         # @Entity @Inheritance(JOINED) — clase base
    │       │   │       ├── EtapaPasteurizacion.java   # @Entity @DiscriminatorValue("PASTEURIZACION")
    │       │   │       ├── EtapaCloruro.java          # @Entity @DiscriminatorValue("CLORURO")
    │       │   │       ├── EtapaCuajo.java            # @Entity @DiscriminatorValue("CUAJO")
    │       │   │       ├── EtapaLavadoDesuerado.java  # @Entity @DiscriminatorValue("LAVADO_DESUERADO")
    │       │   │       ├── EtapaDesuerado.java        # @Entity @DiscriminatorValue("DESUERADO")
    │       │   │       ├── EtapaSalado.java           # @Entity @DiscriminatorValue("SALADO")
    │       │   │       └── EtapaPrensado.java         # @Entity @DiscriminatorValue("PRENSADO")
    │       │   └── dto/
    │       │       ├── LoteRequestDto.java
    │       │       ├── LoteResponseDto.java
    │       │       ├── LoteDetalleDto.java            # Detalle completo con historial de etapas
    │       │       ├── CierreLoteDto.java
    │       │       └── etapa/
    │       │           ├── PasteurizacionDto.java
    │       │           ├── CloruroDto.java
    │       │           ├── CuajoDto.java
    │       │           ├── CorteDto.java
    │       │           ├── LavadoDesueradoDto.java
    │       │           ├── DesueradoDto.java
    │       │           ├── SaladoDto.java
    │       │           └── PrensadoDto.java
    │       │
    │       └── exportacion/                           # Módulo: Exportación a Google Sheets
    │           ├── controller/
    │           │   └── ExportacionController.java
    │           ├── service/
    │           │   └── GoogleSheetsService.java       # Escritura en Sheets
    │           └── dto/
    │               ├── ExportacionLotesRequestDto.java
    │               └── ExportacionResponseDto.java
    │
    └── resources/
        └── application.properties                     # H2 datasource, Sheets credentials path
```

---

## 6. Dependencias (`pom.xml`)

```xml
<!-- Spring Boot Starters -->
<dependency>spring-boot-starter-web</dependency>
<dependency>spring-boot-starter-data-jpa</dependency>
<dependency>spring-boot-starter-validation</dependency>

<!-- Base de datos (prototipo) -->
<dependency>com.h2database:h2</dependency>

<!-- Google Sheets API -->
<dependency>com.google.apis:google-api-services-sheets:v4-rev20220927-2.0.0</dependency>
<dependency>com.google.auth:google-auth-library-oauth2-http:1.19.0</dependency>

<!-- Utilidades -->
<dependency>org.projectlombok:lombok</dependency>
```

---

## 7. Configuración de Google Sheets

El backend usa una **Service Account** de Google Cloud. El archivo de credenciales JSON se almacena en el servidor y nunca se expone al frontend.

**`application.properties`:**

```
sheets.credentials.path=classpath:credentials/service-account.json
sheets.spreadsheet.id=SPREADSHEET_ID_AQUI
```

**Hojas del Spreadsheet:**

| Hoja | Contenido |
| --- | --- |
| `lotes` | Cabecera del lote por fila |
| `etapas` | Una fila por etapa registrada |
| `cortes` | Una fila por corte |
| `cierres` | Datos de cierre y rendimientos |
| `recepciones` | Una fila por recepción de leche |

---

## 8. Pendientes Técnicos

| # | Pendiente | Impacto |
| --- | --- | --- |
| 1 | Confirmar etapas de Lavado/Desuerado por producto | Alto — afecta lógica de transición |
| 2 | Confirmar fórmulas de rendimiento teórico y general | Alto — afecta cierre de lote |
| 3 | Definir tolerancias para clasificación CONDICIONAL en leche | Medio |
| 4 | Confirmar si el batch reinicia cada día o es global | Medio — afecta generación del código |
| 5 | Definir si se necesita autenticación básica en el prototipo | Medio |
| 6 | Obtener credenciales de Google Cloud para la Service Account | Alto — bloqueante para exportación |