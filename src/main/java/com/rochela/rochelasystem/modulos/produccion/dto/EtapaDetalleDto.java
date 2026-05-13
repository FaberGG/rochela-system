package com.rochela.rochelasystem.modulos.produccion.dto;

import com.rochela.rochelasystem.shared.enums.TipoEtapa;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "EtapaDetalle", description = "Detalle de una etapa registrada en el lote")
public class EtapaDetalleDto {

    @Schema(description = "Tipo de etapa", example = "PASTEURIZACION")
    private TipoEtapa tipoEtapa;

    @Schema(description = "Hora de registro", example = "08:45:00")
    private LocalTime hora;

    @Schema(description = "Fecha y hora de registro", example = "2026-05-13T08:45:00")
    private LocalDateTime fechaHoraRegistro;

    @Schema(description = "Temperatura", example = "72.5")
    private Double temperatura;

    @Schema(description = "Cantidad en gramos", example = "12.5")
    private Double cantidadGramos;

    @Schema(description = "Lote del insumo", example = "INS-CL-2026-05")
    private String loteInsumo;

    @Schema(description = "Litros procesados", example = "150.0")
    private Double litros;

    @Schema(description = "Cantidad en kg", example = "20.0")
    private Double cantidadKg;

    @Schema(description = "Sodio inicial", example = "1.2")
    private Double sodioInicial;

    @Schema(description = "Sodio final", example = "1.8")
    private Double sodioFinal;

    @Schema(description = "Lote de sal", example = "SAL-2026-05")
    private String loteSal;

    @Schema(description = "Hora fin", example = "09:30:00")
    private LocalTime horaFin;

    @Schema(description = "Duracion en minutos", example = "45")
    private Integer duracionMinutos;

    @Schema(description = "Presion en psi", example = "20.0")
    private Double presionPsi;

    @Schema(description = "Responsable", example = "Operario 1")
    private String responsable;
}

