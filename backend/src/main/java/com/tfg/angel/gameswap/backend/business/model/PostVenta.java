package com.tfg.angel.gameswap.backend.business.model;

import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "post_venta")
public class PostVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vendedor")
    private Usuario vendedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    private Producto producto;

    private String plataforma;

    private Double precio;

    @Enumerated(EnumType.STRING)
    private EstadoPost estado;

    private String descripcion;
}
