package com.tfg.angel.gameswap.backend.business.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "CompraVenta")
public class CompraVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_post_venta")
    private PostVenta postVenta;

    @ManyToOne
    @JoinColumn(name = "id_comprador")
    private Usuario comprador;

    private Double precio;

    private LocalDate fecha;
}
