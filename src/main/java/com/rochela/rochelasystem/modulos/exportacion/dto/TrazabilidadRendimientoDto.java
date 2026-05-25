package com.rochela.rochelasystem.modulos.exportacion.dto;

import java.time.LocalDateTime;

public record TrazabilidadRendimientoDto(
        String codigoLoteQueso,
        String productoNombre,
        String codigoLoteLeche,
        LocalDateTime fechaHoraInicio,
        LocalDateTime fechaHoraCierre,
        Double litrosTotalesLeche,
        Double grasa,
        Double proteina,
        Double ph,
        Double densidad,
        Double solidosNoGrasos,
        Double solidosTotales,
        Double lactosa,
        Double puntoCrioscopico,
        Double temperatura,
        Double aguaAnadida,
        Integer unidadesProducidas,
        Double pesoTotalKg,
        Double rendimientoTeorico,
        Double rendimientoGeneral
) {
}
