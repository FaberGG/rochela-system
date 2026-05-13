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

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK |
| `nombreEmpresa` | String | Nombre de la empresa o razón social |
| `nombrePropietario` | String | Nombre del dueño o de la finca |
| `nombreMayordomo` | String | Persona de contacto |
| `telefono` | String | Teléfono |
| `correo` | String | Correo |
| `direccion` | String | Dirección o vereda |
| `activo` | Boolean | Activo / inactivo |

---

### `RecepcionLeche`

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK |
| `fechaHora` | LocalDateTime | Timestamp de recepción |
| `proveedor` | Proveedor | Relación |
| `loteTanque` | String | Identificador del lote de tanque |
| `ubicacion` | UbicacionTanque | TANQUE_1, TANQUE_2, TANQUE_3, CUARTO_FRIO, PROCESO |
| `cantidadLitros` | Double | Litros recibidos |
| `grasa` | Double | % |
| `solidos` | Double | % |
| `proteina` | Double | % |
| `puntoCongelacion` | Double | °C |
| `temperatura` | Double | °C |
| `densidad` | Double | g/mL |
| `lactosa` | Double | % |
| `solidosTotales` | Double | % |
| `aguaAnadida` | Double | % |
| `ph` | Double | — |
| `resultadoValidacion` | ResultadoValidacion | APTA, NO_APTA, CONDICIONAL |
| `observaciones` | String | Texto libre |

---

### `Lote`

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK |
| `codigoLote` | String | Único. Formato L[DDD][AA][B] |
| `producto` | Producto | Relación |
| `recepcionLeche` | RecepcionLeche | Nullable |
| `fechaHoraInicio` | LocalDateTime | Inicio del lote |
| `fechaVencimiento` | LocalDate | Fecha de vencimiento |
| `estadoActual` | EstadoLote | Estado actual del lote |
| `batchDelDia` | Integer | Número de batch diario |
| `observaciones` | String | Texto libre |

---

### `CierreLote`

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK |
| `lote` | Lote | Relación 1:1 |
| `fechaHoraCierre` | LocalDateTime | Cierre |
| `unidadesProducidas` | Integer | Unidades |
| `pesoTotalKg` | Double | Peso total |
| `rendimientoTeorico` | Double | Calculado |
| `rendimientoGeneral` | Double | Calculado |

---

### `EtapaRegistro` (base)

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK |
| `lote` | Lote | Relación |
| `tipoEtapa` | TipoEtapa | PASTEURIZACION, CLORURO, CUAJO, LAVADO_DESUERADO, DESUERADO, SALADO, PRENSADO |
| `hora` | LocalTime | Hora operativa |
| `fechaHoraRegistro` | LocalDateTime | Timestamp de registro |

---

### `EtapaPasteurizacion`

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK y FK a EtapaRegistro |
| `temperatura` | Double | °C |

---

### `EtapaCloruro`

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK y FK a EtapaRegistro |
| `temperatura` | Double | °C |
| `cantidadGramos` | Double | g |
| `loteCloruro` | String | Lote de insumo |

---

### `EtapaCuajo`

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK y FK a EtapaRegistro |
| `temperatura` | Double | °C |
| `cantidadGramos` | Double | g |
| `loteCuajo` | String | Lote de insumo |

---

### `EtapaLavadoDesuerado`

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK y FK a EtapaRegistro |
| `litros` | Double | L |

---

### `EtapaDesuerado`

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK y FK a EtapaRegistro |
| `litros` | Double | L |

---

### `EtapaSalado`

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK y FK a EtapaRegistro |
| `temperatura` | Double | °C |
| `cantidadKg` | Double | kg |
| `sodioInicial` | Double | % |
| `sodioFinal` | Double | % |
| `loteSal` | String | Lote de sal |

---

### `EtapaPrensado`

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK y FK a EtapaRegistro |
| `horaFin` | LocalTime | Fin |
| `duracionMinutos` | Integer | Calculada |
| `presionPsi` | Double | PSI |
| `responsable` | String | Responsable |

---

### `Corte`

| Campo | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | PK |
| `lote` | Lote | Relación |
| `numeroCorte` | Integer | Secuencia |
| `hora` | LocalTime | Hora del corte |
| `observacion` | String | Texto libre |
| `fechaHoraRegistro` | LocalDateTime | Timestamp de registro |

