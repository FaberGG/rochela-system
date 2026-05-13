# Arquitectura y modulos

## Stack

- Spring Boot 3.x
- Java 21
- H2 (prototipo)
- Google Sheets API

## Modulos principales

- `catalogo`: catalogo de productos.
- `proveedores`: gestion de proveedores.
- `recepcion`: recepcion de leche y validaciones.
- `produccion`: lotes, etapas y cierres.
- `exportacion`: exportacion a Google Sheets.
- `shared`: enums, excepciones, configuracion comun.

## Principios

- Separacion por modulos funcionales.
- DTOs para entrada y salida de controllers.
- Servicios orquestan reglas de negocio.
- Repositorios encapsulan acceso a datos.

