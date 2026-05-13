# Integracion con Google Sheets

El backend usa una Service Account de Google Cloud. El archivo de credenciales JSON se almacena en el servidor y nunca se expone al frontend.

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
| `recepciones` | Una fila por recepcion de leche |

