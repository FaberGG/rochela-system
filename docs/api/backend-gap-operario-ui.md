# Backend gap analysis for operario UI

This document summarizes data gaps between the requested operario UI and the current OpenAPI spec (docs/api/openapi.json). It also proposes backend additions or response extensions to avoid N+1 calls and to expose missing fields.

## Summary

Implemented additions or extensions:
- Recepciones pendientes (reductasa): GET /api/v1/recepciones/pendientes incluye `fechaHora`.
- Lotes activos feed: `LoteResumenResponse` expone `etapaActualInicio` y `porcentajeCompletado`.
- Endpoint agregado para feed de operario: GET /api/v1/operario/feed.

Optional additions (performance / UX):
- Server-side drafts for recepcion (if drafts must survive device/session).
- Search endpoint if prefetching is too heavy for large datasets.

## Current endpoints that already cover the UI needs

Recepciones:
- GET /api/v1/recepciones (list, includes fechaHora, ubicacion, litros, estadoRecepcion, resultadoValidacion, proveedorId).
- GET /api/v1/recepciones/pendientes (list pending reductasa, includes proveedor name and minutosTranscurridos).
- PATCH /api/v1/recepciones/{id}/reductasa (close reductasa).

Produccion:
- GET /api/v1/lotes (filter by estado, productoCodigo, fechas, soloActivos).
- GET /api/v1/lotes/{id} (detalle completo con etapas y cortes).
- POST /api/v1/lotes/{id}/prensado/cierre (cerrar prensado).
- POST /api/v1/lotes/{id}/cortes, /cortes/cerrar, /cloruro, /cuajo, /pasteurizacion, /lavado-desuerado, /desuerado, /salado, /cierre (siguiente etapa segun estado).

Catalogos:
- GET /api/v1/productos (incluye flags requierePasteurizacion / requiereCloruro / requiereLavadoDesuerado).

## Gaps that require backend changes

### 1) Pendientes de reductasa: falta hora de recepcion

Estado: resuelto. GET /api/v1/recepciones/pendientes incluye `fechaHora`.

### 2) Lotes activos: falta inicio de etapa y porcentaje

Estado: resuelto. `LoteResumenResponse` incluye `etapaActualInicio` y `porcentajeCompletado`.

## Optional additions (performance / UX)

### A) Endpoint agregado para feed de operario

Estado: implementado. Disponible en GET /api/v1/operario/feed.

### B) Borradores de recepcion

Motivo:
- La UI muestra borradores (formularios abandonados).
- Si deben persistir entre dispositivos/sesiones, se necesita backend.

Propuesta:
- POST /api/v1/recepciones/borradores
- GET /api/v1/recepciones/borradores
- DELETE /api/v1/recepciones/borradores/{id}

Si los borradores son locales (LocalStorage/IndexedDB), no se requiere backend.

### C) Busqueda server-side (si el volumen es alto)

Motivo:
- Busqueda "search as you type" con prefetching es viable para datasets pequenos.
- Si crece mucho, se recomienda un endpoint con query.

Propuesta:
- GET /api/v1/recepciones?query=... (o campo dedicado)
- GET /api/v1/lotes?query=...

## Conclusion

Con el backend actual se puede implementar el feed del operario sin N+1:
1) `fechaHora` en pendientes de reductasa,
2) `etapaActualInicio` y `porcentajeCompletado` en resumen de lotes,
3) endpoint agregado /api/v1/operario/feed.
