# Produccion en Render

Esta guia describe como configurar el backend para produccion en Render y como simular un entorno de produccion antes del despliegue. Esta basada en `application.yml` y el Dockerfile del repositorio.

## Estado actual

- `application.yml` usa variables de entorno y valores seguros para produccion (por ejemplo `ddl-auto: validate` y `show-sql: false`).
- `application-prod.yml` contiene valores locales (URL localhost, credenciales y `ddl-auto: update`). Para produccion, reemplaza esos valores por variables de entorno o evita usar ese perfil hasta ajustarlo.

## Variables de entorno

Las siguientes variables se leen desde `application.yml`:

| Variable | Obligatoria | Descripcion | Sensible |
| --- | --- | --- | --- |
| `SPRING_DATASOURCE_URL` | Si | JDBC URL de Postgres. | No |
| `SPRING_DATASOURCE_USERNAME` | Si | Usuario de base de datos. | Si |
| `SPRING_DATASOURCE_PASSWORD` | Si | Password de base de datos. | Si |
| `SERVER_PORT` | Si (Render) | Puerto del servidor. En Render debe coincidir con `PORT`. | No |
| `CORS_ALLOWED_ORIGINS` | Si | Lista CSV de dominios permitidos. Si esta vacio, CORS queda deshabilitado. | No |
| `CORS_ALLOWED_METHODS` | No | CSV de metodos permitidos. | No |
| `CORS_ALLOWED_HEADERS` | No | CSV de headers permitidos. | No |
| `CORS_ALLOW_CREDENTIALS` | No | `true` o `false`. | No |
| `CORS_MAX_AGE` | No | Segundos de cache. | No |
| `EXPORTACION_GOOGLE_SPREADSHEET_ID` | Si (si exportacion activa) | Spreadsheet ID. | Si |
| `EXPORTACION_GOOGLE_CREDENTIALS_BASE64` | Si (si exportacion activa) | JSON de Service Account en base64. | Si |
| `EXPORTACION_GOOGLE_CREDENTIALS_PATH` | No | Ruta local a JSON si no usas base64. | Si |
| `EXPORTACION_GOOGLE_APPLICATION_NAME` | No | Nombre de la app para Google API. | No |
| `EXPORTACION_GOOGLE_SHEET_RENDIMIENTO` | Si (si exportacion activa) | Nombre de hoja. | No |
| `EXPORTACION_GOOGLE_SHEET_RECEPCIONES` | Si (si exportacion activa) | Nombre de hoja. | No |
| `EXPORTACION_GOOGLE_SHEET_ETAPAS` | Si (si exportacion activa) | Nombre de hoja. | No |
| `EXPORTACION_GOOGLE_SHEET_CALIDAD_POR_PROVEEDOR` | Si (si exportacion activa) | Nombre de hoja para la vista `v_calidad_por_proveedor`. | No |
| `EXPORTACION_GOOGLE_SHEET_TRAZABILIDAD_LOTES` | Si (si exportacion activa) | Nombre de hoja para la vista `v_trazabilidad_lote_completa`. | No |
| `EXPORTACION_GOOGLE_SHEET_PRODUCCION_ETAPAS_COMPLETAS` | Si (si exportacion activa) | Nombre de hoja para la vista `v_produccion_etapas_completas`. | No |
| `EXPORTACION_GOOGLE_SHEET_CORTES_POR_LOTE` | Si (si exportacion activa) | Nombre de hoja para la vista `v_cortes_por_lote`. | No |
| `EXPORTACION_RECEPCION_PENDIENTE_ENABLED` | No | `true` para habilitar exportacion automatica. | No |

Notas:

- Para Google Sheets debes definir **solo uno** entre `EXPORTACION_GOOGLE_CREDENTIALS_BASE64` o `EXPORTACION_GOOGLE_CREDENTIALS_PATH`.
- La base64 puede contener saltos de linea; el backend los limpia antes de decodificar.

## Render: despliegue recomendado

1. Crea una base de datos en Render (PostgreSQL).
2. Crea un **Web Service** y selecciona **Docker** (usa el `Dockerfile` del repo).
3. Configura variables de entorno en Render.
4. Despliega.

### Conexion JDBC en Render

Render entrega un host, puerto y nombre de base de datos. La URL JDBC esperada es:

```
jdbc:postgresql://<HOST>:<PORT>/<DB_NAME>
```

Configura:

- `SPRING_DATASOURCE_URL` con la URL JDBC.
- `SPRING_DATASOURCE_USERNAME` y `SPRING_DATASOURCE_PASSWORD` con las credenciales de Render.
- `SERVER_PORT` con el mismo valor de `PORT` (en Render suele ser `10000`).

## Credenciales seguras

- Usa cuentas de servicio (no cuentas personales) para Google Sheets.
- Genera passwords largos y unicos para base de datos (>= 24 caracteres).
- No reutilices las credenciales de `docker-compose.yml` en produccion.
- Guarda secretos solo en el panel de Render (nunca en el repositorio).
- Separa credenciales por ambiente (dev, staging, prod).

## Usuario solo lectura (Postgres)

Si un usuario solo lectura se conectara mas adelante, **si** conviene crear un usuario dedicado con permisos minimos. Render no tiene UI para usuarios, pero puedes crearlos conectandote con el usuario administrador y ejecutando SQL:

```
CREATE USER rochela_readonly WITH PASSWORD '<password-segura>';
GRANT CONNECT ON DATABASE rochela_db TO rochela_readonly;
GRANT USAGE ON SCHEMA public TO rochela_readonly;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO rochela_readonly;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO rochela_readonly;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT SELECT ON TABLES TO rochela_readonly;
```

Guarda estas credenciales como variables de entorno separadas si las necesitas en servicios de reporting.

## Docker-compose (solo para local)

El archivo `docker-compose.yml` crea un Postgres local para desarrollo. No lo uses para produccion. Para simular prod localmente:

1. Levanta Postgres:

```
docker compose up -d
```

2. Ejecuta el backend con variables de entorno de produccion (ver seccion anterior).

## Pruebas para produccion y simulacion local

1. Ejecuta tests unitarios:

```
mvn test
```

2. Construye la imagen o el JAR como en produccion:

```
mvn -DskipTests package
```

3. Ejecuta con variables de entorno de produccion:

```
set SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/rochela_db
set SPRING_DATASOURCE_USERNAME=rochela_user
set SPRING_DATASOURCE_PASSWORD=rochela_password
set SERVER_PORT=8080
set CORS_ALLOWED_ORIGINS=http://localhost:3000
java -jar target\rochelasystem-0.0.1-SNAPSHOT.jar
```

4. Verifica:

- Conexion a base de datos (arranque sin errores de JDBC).
- CORS con un `Origin` permitido.
- Exportacion a Google Sheets si esta habilitada.
- Endpoints criticos del negocio.
