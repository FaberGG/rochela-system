package com.rochela.rochelasystem.modulos.produccion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lote_leche_recepcion",
        uniqueConstraints = @UniqueConstraint(columnNames = "recepcion_leche_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Descarga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_leche_id", nullable = false)
    private LoteLeche loteLeche;

    @Column(name = "recepcion_leche_id", nullable = false)
    private Long recepcionLecheId;
}
