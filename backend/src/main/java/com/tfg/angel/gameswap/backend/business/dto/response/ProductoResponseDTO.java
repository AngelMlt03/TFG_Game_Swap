package com.tfg.angel.gameswap.backend.business.dto.response;

import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoResponseDTO {

    private Long id;
    private Integer idAPI;
    private String nombre;
    private EstadoProducto estado;
}
