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
@Table(name = "Post_Intercambio")
public class PostIntercambio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto_cambio")
    private Producto productoCambio;

    private String plataforma;

    private String plataformaCambio;

    @Enumerated(EnumType.STRING)
    private EstadoPost estado;

    private String descripcion;
}
