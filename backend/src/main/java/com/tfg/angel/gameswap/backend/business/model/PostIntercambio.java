package com.tfg.angel.gameswap.backend.business.model;

import jakarta.persistence.*;

@Entity
public class PostIntercambio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_producto_cambio")
    private Producto productoCambio;
}
