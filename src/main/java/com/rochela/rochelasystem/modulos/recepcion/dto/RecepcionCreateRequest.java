package com.rochela.rochelasystem.modulos.recepcion.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.rochela.rochelasystem.shared.enums.AnalisisOrganoleptico;
import com.rochela.rochelasystem.shared.enums.Jornada;
import com.rochela.rochelasystem.shared.enums.ResultadoAlcohol;
import com.rochela.rochelasystem.shared.enums.UbicacionTanque;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
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
@Schema(name = "RecepcionCreateRequest", description = "Datos para registrar una recepcion de leche")
public class RecepcionCreateRequest {

    @Schema(description = "Fecha de recepcion", example = "2026-05-13")
    private LocalDate fecha;

    @Schema(description = "Id del proveedor", example = "10")
    private Long proveedorId;

    @Schema(description = "Jornada", example = "AM")
    private Jornada jornada;

    @Schema(description = "Ubicacion del tanque", example = "TANQUE_1")
    private UbicacionTanque ubicacion;

    @Schema(description = "Cantidad de litros", example = "150.0")
    private Double cantidadLitros;

    @Schema(description = "Responsable del registro", example = "Operario 1")
    private String realizadoPor;

    @JsonAlias({"analisisOrganoléptico"})
    @Schema(description = "Resultado del analisis organoleptico", example = "CUMPLE")
    private AnalisisOrganoleptico analisisOrganoleptico;

    @Schema(description = "Cumple color", example = "true")
    private Boolean colorCumple;

    @Schema(description = "Cumple olor", example = "true")
    private Boolean olorCumple;

    @Schema(description = "Resultado de alcohol", example = "NEGATIVO")
    private ResultadoAlcohol alcohol;

    @Schema(description = "Temperatura", example = "8.5")
    private Double temperatura;

    @Schema(description = "Densidad", example = "1.03")
    private Double densidad;

    @Schema(description = "PH", example = "6.6")
    private Double ph;

    @Schema(description = "Proteina", example = "3.2")
    private Double proteina;

    @Schema(description = "Grasa", example = "3.5")
    private Double grasa;

    @Schema(description = "Opcional. Solidos no grasos (SNF)", example = "8.1",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double solidosNoGrasos;

    @Schema(description = "Solidos totales", example = "12.3")
    private Double solidosTotales;

    @Schema(description = "Acidez titulable", example = "0.13")
    private Double acidezTitulable;

    @Schema(description = "Opcional. Lactosa", example = "4.8",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double lactosa;

    @Schema(description = "Agua anadida", example = "0.0")
    private Double aguaAnadida;

    @Schema(description = "Punto crioscopico", example = "-0.52")
    private Double puntoCrioscopico;

    @Schema(description = "Opcional. Sales", example = "0.6",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double sales;

    @Schema(description = "Hora inicio reductasa", example = "09:00:00")
    private LocalTime horaInicioReductasa;

    @Schema(description = "Observaciones", example = "Sin novedades")
    private String observaciones;
}
