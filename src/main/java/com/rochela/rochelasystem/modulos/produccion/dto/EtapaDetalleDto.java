package com.rochela.rochelasystem.modulos.produccion.dto;

import com.rochela.rochelasystem.shared.enums.TipoEtapa;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EtapaDetalleDto {

    private TipoEtapa tipoEtapa;
    private LocalTime hora;
    private LocalDateTime fechaHoraRegistro;
    private Double temperatura;
    private Double cantidadGramos;
    private String loteInsumo;
    private Double litros;
    private Double cantidadKg;
    private Double sodioInicial;
    private Double sodioFinal;
    private String loteSal;
    private LocalTime horaFin;
    private Integer duracionMinutos;
    private Double presionPsi;
    private String responsable;
}

