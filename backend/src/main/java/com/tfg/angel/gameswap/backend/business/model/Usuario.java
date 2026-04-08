package com.tfg.angel.gameswap.backend.business.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(name = "n_usuario", unique = true, nullable = false)
    private String nombreUsuario;

    private LocalDate fechaNacimiento;

    private Double saldo = 0.0;

    @Column(unique = true, nullable = false)
    private String correo;

    private Double estrellas = 0.0;

    // Relaciones

    @OneToMany(mappedBy = "comprador", fetch = FetchType.LAZY)
    private List<CompraVenta> compras;

    @OneToMany(mappedBy = "reviewer", fetch = FetchType.LAZY)
    private List<Review> reviewsRealizadas;

    @OneToMany(mappedBy = "reviewed", fetch = FetchType.LAZY)
    private List<Review> reviewsRecibidas;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Carrito> carritos;
}
