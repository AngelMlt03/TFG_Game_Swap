package com.tfg.angel.gameswap.backend.business.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class CompraVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_comprador")
    private Usuario comprador;

    @ManyToOne
    @JoinColumn(name = "id_vendedor")
    private Usuario vendedor;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    private Double precio;

    private LocalDate fecha = LocalDate.now();
}
