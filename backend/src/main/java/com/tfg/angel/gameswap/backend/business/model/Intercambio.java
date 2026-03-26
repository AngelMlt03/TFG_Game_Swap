package com.tfg.angel.gameswap.backend.business.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Intercambio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_cambio")
    private Producto productoCambio;

    @ManyToOne
    @JoinColumn(name = "id_usuario_producto")
    private Usuario usuarioProducto;

    @ManyToOne
    @JoinColumn(name = "id_usuario_cambio")
    private Usuario usuarioCambio;

    private LocalDate fecha = LocalDate.now();
}
