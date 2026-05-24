package com.rochela.rochelasystem.modulos.recepcion.dto;

import com.rochela.rochelasystem.shared.enums.EstadoRecepcion;
import com.rochela.rochelasystem.shared.enums.Jornada;
import com.rochela.rochelasystem.shared.enums.ResultadoValidacion;
import com.rochela.rochelasystem.shared.enums.UbicacionTanque;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(name = "RecepcionDisponibleParaLote", description = "Recepcion lista para mezclarse en un lote de leche")
public class RecepcionDisponibleParaLoteDto {

    @Schema(description = "Id de la recepcion", example = "1")
    private final Long id;

    @Schema(description = "Id del proveedor", example = "10")
    private final Long proveedorId;

    @Schema(description = "Nombre del proveedor", example = "Lacteos del Norte")
    private final String proveedor;

    @Schema(description = "Fecha y hora de la recepcion", example = "2026-05-13T09:05:00")
    private final LocalDateTime fechaHora;

    @Schema(description = "Jornada", example = "AM")
    private final Jornada jornada;

    @Schema(description = "Ubicacion del tanque", example = "TANQUE_1")
    private final UbicacionTanque ubicacion;

    @Schema(description = "Cantidad de litros", example = "150.0")
    private final Double cantidadLitros;

    @Schema(description = "Resultado de validacion", example = "APTA")
    private final ResultadoValidacion resultadoValidacion;

    @Schema(description = "Estado de la recepcion", example = "COMPLETA")
    private final EstadoRecepcion estadoRecepcion;
}
