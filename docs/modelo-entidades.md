
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

![/images/lote-estados.png](../../../../bb57c93a-7dd9-4351-8fd8-720410985a8d_ExportBlock-d73d2eef-9d97-4d10-80c9-7dd5012d10c8/ExportBlock-d73d2eef-9d97-4d10-80c9-7dd5012d10c8-Part-1/image.png)

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