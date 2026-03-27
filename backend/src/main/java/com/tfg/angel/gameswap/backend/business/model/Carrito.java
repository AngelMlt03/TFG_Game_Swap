package com.tfg.angel.gameswap.backend.business.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private Double coste = 0.0;

    @OneToMany(mappedBy = "carrito", fetch = FetchType.LAZY)
    private List<ProductoCarrito> productos;
}
