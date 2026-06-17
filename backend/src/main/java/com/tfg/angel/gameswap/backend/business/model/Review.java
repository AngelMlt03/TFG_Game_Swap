package com.tfg.angel.gameswap.backend.business.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_reviewer")
    private Usuario reviewer;

    @ManyToOne
    @JoinColumn(name = "id_reviewed")
    private Usuario reviewed;

    private String contenido;

    private Double estrellas;

    private String tipoReview;

    @ManyToOne
    @JoinColumn(name = "id_compra_venta")
    private CompraVenta compraVenta;

    @ManyToOne
    @JoinColumn(name = "id_intercambio")
    private Intercambio intercambio;
}
