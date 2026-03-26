package com.tfg.angel.gameswap.backend.business.model;

import jakarta.persistence.*;

@Entity
public class PostVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_vendedor")
    private Usuario vendedor;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    private Double precio;
}
