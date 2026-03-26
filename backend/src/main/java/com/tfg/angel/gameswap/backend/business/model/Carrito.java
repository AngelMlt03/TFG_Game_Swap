package com.tfg.angel.gameswap.backend.business.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private Double coste = 0.0;

    @OneToMany(mappedBy = "carrito")
    private List<ProductoCarrito> productos;
}
