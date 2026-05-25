package com.rochela.rochelasystem.modulos.produccion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
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
@Schema(name = "LoteLecheResponse", description = "Detalle del lote de leche")
public class LoteLecheResponse {

    @Schema(description = "Id del lote de leche", example = "1")
    private Long id;

    @Schema(description = "Codigo interno de la mezcla", example = "LL-170526-01")
    private String codigoLoteLeche;

    @Schema(description = "Fecha y hora del lote de leche", example = "2026-05-17T08:30:00")
    private LocalDateTime fechaHora;

    @Schema(description = "Litros totales de la mezcla", example = "1200.5")
    private Double cantidadLitrosTotal;

    @Schema(description = "Litros disponibles para produccion", example = "750.0")
    private Double cantidadLitrosDisponibles;

    @Schema(description = "Tanque donde se hizo la mezcla", example = "SILO_1")
    private String tanqueProceso;

    @Schema(description = "Responsable de la mezcla", example = "Juan Perez")
    private String realizadoPor;

    @Schema(description = "Ids de recepciones usadas en la mezcla", example = "[10, 11, 12]")
    private List<Long> recepcionLecheIds;

    @Schema(description = "Grasa")
    private Double grasa;

    @Schema(description = "Solidos no grasos")
    private Double solidosNoGrasos;

    @Schema(description = "Temperatura")
    private Double temperatura;

    @Schema(description = "Proteina")
    private Double proteina;

    @Schema(description = "Punto crioscopico")
    private Double puntoCrioscopico;

    @Schema(description = "Densidad")
    private Double densidad;

    @Schema(description = "Lactosa")
    private Double lactosa;

    @Schema(description = "Solidos totales")
    private Double solidosTotales;

    @Schema(description = "Agua anadida")
    private Double aguaAnadida;

    @Schema(description = "PH")
    private Double ph;

    @Schema(description = "Observaciones")
    private String observaciones;
}
