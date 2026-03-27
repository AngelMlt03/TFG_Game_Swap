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
public class Intercambio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cambio")
    private Producto productoCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_producto")
    private Usuario usuarioProducto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_cambio")
    private Usuario usuarioCambio;

    private LocalDate fecha = LocalDate.now();
}
