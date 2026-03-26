package com.tfg.angel.gameswap.backend.business.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer idAPI;

    private String nombre;

    private String estado;

    // Relaciones

    @OneToMany(mappedBy = "producto")
    private List<CompraVenta> comprasVentas;

    @OneToMany(mappedBy = "producto")
    private List<PostVenta> postsVenta;

    @OneToMany(mappedBy = "producto")
    private List<PostIntercambio> postsIntercambio;

    @OneToMany(mappedBy = "producto")
    private List<ProductoCarrito> productosCarrito;
}
