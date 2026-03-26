package com.tfg.angel.gameswap.backend.business.model;

import jakarta.persistence.*;

@Entity
public class ProductoCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_carrito")
    private Carrito carrito;
}
