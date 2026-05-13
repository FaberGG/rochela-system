package com.rochela.rochelasystem.modulos.recepcion.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.rochela.rochelasystem.shared.enums.AnalisisOrganoleptico;
import com.rochela.rochelasystem.shared.enums.Jornada;
import com.rochela.rochelasystem.shared.enums.ResultadoAlcohol;
import com.rochela.rochelasystem.shared.enums.UbicacionTanque;
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
public class RecepcionCreateRequest {

    private LocalDate fecha;
    private Long proveedorId;
    private Jornada jornada;
    private UbicacionTanque ubicacion;
    private Double cantidadLitros;
    private String realizadoPor;

    @JsonAlias({"analisisOrganoléptico"})
    private AnalisisOrganoleptico analisisOrganoleptico;

    private Boolean colorCumple;
    private Boolean olorCumple;
    private ResultadoAlcohol alcohol;

    private Double temperatura;
    private Double densidad;
    private Double ph;
    private Double proteina;
    private Double grasa;
    private Double solidosTotales;
    private Double acidezTitulable;
    private Double aguaAnadida;
    private Double puntoCrioscopico;

    private LocalTime horaInicioReductasa;
    private String observaciones;
}
