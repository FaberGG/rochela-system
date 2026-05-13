# Errores y excepciones

Este documento describe el formato estandar de errores del API y los codigos de error expuestos.

## Formato de respuesta

```json
{
  "timestamp": "2026-05-13T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Datos de entrada invalidos",
  "path": "/api/v1/lotes/5",
  "code": "VALIDACION_INVALIDA"
}
```

## Codigos de error

**Errores de dominio (LacteaException)**

- `ESTADO_INVALIDO` - Transicion de estado no permitida.
- `ETAPA_NO_APLICA` - La etapa no aplica al producto o al estado actual.
- `LOTE_NO_ENCONTRADO` - Lote inexistente.
- `RECEPCION_NO_ENCONTRADA` - Recepcion inexistente.

**Errores de validacion**

- `VALIDACION_INVALIDA` - Fallas de validacion en DTOs o restricciones.
- `CUERPO_INVALIDO` - JSON mal formado o campos incompatibles.

**Errores HTTP genericos**

- `ERROR_HTTP_<status>` - Errores propagados por `ResponseStatusException`.

**Errores inesperados**

- `ERROR_INTERNO` - Excepcion no controlada.

## Referencia tecnica

- Handler: `src/main/java/com/rochela/rochelasystem/shared/exception/GlobalExceptionHandler.java`
- DTO: `src/main/java/com/rochela/rochelasystem/shared/exception/ErrorResponseDto.java`

