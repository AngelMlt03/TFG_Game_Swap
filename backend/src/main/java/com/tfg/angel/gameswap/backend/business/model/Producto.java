package com.tfg.angel.gameswap.backend.business.model;

import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer idAPI;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private EstadoProducto estado;

    // Relaciones

    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY)
    private List<CompraVenta> comprasVentas;

    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY)
    private List<PostVenta> postsVenta;

    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY)
    private List<PostIntercambio> postsIntercambio;

    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY)
    private List<ProductoCarrito> productosCarrito;
}
