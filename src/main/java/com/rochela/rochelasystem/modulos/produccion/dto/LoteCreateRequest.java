package com.rochela.rochelasystem.modulos.produccion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "LoteCreateRequest", description = "Datos para crear un lote de produccion")
public class LoteCreateRequest {

    @Schema(description = "Codigo del producto", example = "QUESO-001")
    private String productoCodigo;

    @Schema(description = "Fecha y hora de inicio", example = "2026-05-13T08:30:00")
    private LocalDateTime fechaHoraInicio;

    @Schema(description = "Id del lote de leche", example = "5")
    private Long loteLecheId;

    @Schema(description = "Opcional. Grasa", example = "3.4", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double grasa;

    @Schema(description = "Opcional. Solidos no grasos (SNF)", example = "8.1",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double solidosNoGrasos;

    @Schema(description = "Opcional. Proteina", example = "3.2", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double proteina;

    @Schema(description = "Opcional. Punto de congelacion", example = "-0.52",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double puntoCrioscopico;

    @Schema(description = "Opcional. Temperatura", example = "8.5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double temperatura;

    @Schema(description = "Opcional. Densidad", example = "1.03", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double densidad;

    @Schema(description = "Opcional. Lactosa", example = "4.8", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double lactosa;

    @Schema(description = "Opcional. Solidos totales", example = "12.3",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double solidosTotales;

    @Schema(description = "Opcional. Agua anadida", example = "0.0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double aguaAnadida;

    @Schema(description = "Opcional. Potencial de hidrogeno", example = "6.6",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double ph;

    @Schema(description = "Opcional. Sales", example = "0.6", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double sales;
}
