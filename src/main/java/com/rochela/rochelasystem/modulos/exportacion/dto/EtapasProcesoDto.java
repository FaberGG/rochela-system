package com.rochela.rochelasystem.modulos.exportacion.dto;

public record EtapasProcesoDto(
        String codigoLoteQueso,
        String productoNombre,
        Double rendimientoGeneral,
        Double pesoTotalKg,
        Double pasteurizacionTemp,
        Double cloruroTemp,
        Double cloruroGramos,
        Double cuajoTemp,
        Double cuajoGramos,
        Double lavadoDesueradoLitros,
        Double desueradoLitros,
        Double saladoTemp,
        Double saladoCantidadKg,
        Double saladoSodioInicial,
        Double saladoSodioFinal,
        Double prensadoPresionPsi,
        Integer prensadoDuracionMinutos,
        Integer cantidadCortes
) {
}
