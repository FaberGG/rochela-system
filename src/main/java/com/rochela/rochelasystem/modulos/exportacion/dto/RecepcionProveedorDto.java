package com.rochela.rochelasystem.modulos.exportacion.dto;

import java.time.LocalDate;

public record RecepcionProveedorDto(
        Long recepcionId,
        LocalDate fecha,
        String jornada,
        String nombreRuta,
        String nombreRecolector,
        Double cantidadLitros,
        String resultadoValidacion,
        String estadoRecepcion,
        Double temperatura,
        Double ph,
        Double densidad,
        Double grasa,
        Double proteina,
        Double acidezTitulable,
        Double puntoCrioscopico,
        Integer tiempoReductasaMinutos
) {
}
