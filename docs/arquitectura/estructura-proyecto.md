# Estructura del proyecto

```
src/
└── main/
    ├── java/com/rochela/rochelasystem
    │   │
    │   ├── shared/                                    # Codigo transversal a todos los modulos
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
    │       ├── proveedores/                           # Modulo: Proveedores
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
    │       ├── catalogo/                              # Modulo: Catalogo de productos
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
    │       ├── recepcion/                             # Modulo: Recepcion de leche
    │       │   ├── controller/
    │       │   │   └── RecepcionLecheController.java
    │       │   ├── service/
    │       │   │   ├── RecepcionService.java          # Orquestacion del flujo de recepcion
    │       │   │   └── ValidacionFisicoquimicaService.java  # Validacion de rangos por parametro
    │       │   ├── repository/
    │       │   │   └── RecepcionLecheRepository.java
    │       │   ├── model/
    │       │   │   └── RecepcionLeche.java
    │       │   └── dto/
    │       │       ├── RecepcionRequestDto.java
    │       │       ├── RecepcionResponseDto.java
    │       │       ├── ReductasaCierreDto.java        # PATCH hora fin reductasa
    │       │       └── ValidacionDetalleDto.java      # Resultado por parametro
    │       │
    │       ├── produccion/                            # Modulo: Produccion de queso
    │       │   ├── controller/
    │       │   │   ├── LoteController.java
    │       │   │   └── EtapaController.java           # Endpoints de registro por etapa
    │       │   ├── service/
    │       │   │   ├── LoteService.java               # Orquestacion y transicion de estados
    │       │   │   ├── LoteadoService.java            # Generacion del codigo de lote
    │       │   │   ├── EtapaService.java              # Persistencia de etapas y validacion de transicion
    │       │   │   └── RendimientoService.java        # Calculo de indices al cierre
    │       │   ├── repository/
    │       │   │   ├── LoteRepository.java
    │       │   │   ├── CierreLoteRepository.java
    │       │   │   ├── EtapaRegistroRepository.java   # Repositorio base (consultas polimorficas)
    │       │   │   └── CorteRepository.java
    │       │   ├── model/
    │       │   │   ├── Lote.java
    │       │   │   ├── CierreLote.java
    │       │   │   ├── Corte.java
    │       │   │   └── etapa/
    │       │   │       ├── EtapaRegistro.java         # @Entity @Inheritance(JOINED) - clase base
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
    │       └── exportacion/                           # Modulo: Exportacion a Google Sheets
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

