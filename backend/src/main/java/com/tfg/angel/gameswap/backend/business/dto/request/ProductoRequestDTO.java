package com.tfg.angel.gameswap.backend.business.dto.request;

import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoRequestDTO {

    private Integer idAPI;
    private String nombre;
    private EstadoProducto estado;
}
