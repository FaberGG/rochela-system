package com.rochela.rochelasystem.modulos.produccion.dto;

import com.rochela.rochelasystem.shared.enums.EstadoLote;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "LoteResumenResponse", description = "Resumen del lote")
public class LoteResumenResponse {

    @Schema(description = "Id del lote", example = "1")
    private Long id;

    @Schema(description = "Codigo del lote", example = "L-20260513-01")
    private String codigoLote;

    @Schema(description = "Producto asociado")
    private ProductoResumenDto producto;

    @Schema(description = "Fecha y hora de inicio", example = "2026-05-13T08:30:00")
    private LocalDateTime fechaHoraInicio;

    @Schema(description = "Fecha de vencimiento", example = "2026-06-13")
    private LocalDate fechaVencimiento;

    @Schema(description = "Opcional. Grasa", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double grasa;

    @Schema(description = "Opcional. Solidos no grasos (SNF)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double solidosNoGrasos;

    @Schema(description = "Opcional. Proteina", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double proteina;

    @Schema(description = "Opcional. Punto de congelacion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double puntoCrioscopico;

    @Schema(description = "Opcional. Temperatura", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double temperatura;

    @Schema(description = "Opcional. Densidad", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double densidad;

    @Schema(description = "Opcional. Lactosa", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double lactosa;

    @Schema(description = "Opcional. Solidos totales", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double solidosTotales;

    @Schema(description = "Opcional. Agua anadida", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double aguaAnadida;

    @Schema(description = "Opcional. Potencial de hidrogeno", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double ph;

    @Schema(description = "Opcional. Sales", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double sales;

    @Schema(description = "Estado actual del lote", example = "INICIADO")
    private EstadoLote estadoActual;

    @Schema(description = "Siguiente etapa esperada", example = "PASTEURIZACION")
    private EstadoLote siguienteEtapa;

    @Schema(description = "Inicio de la etapa actual", example = "2026-05-13T11:30:00")
    private LocalDateTime etapaActualInicio;

    @Schema(description = "Porcentaje completado del proceso", example = "70.0")
    private Double porcentajeCompletado;
}
