package com.tfg.angel.gameswap.backend.business.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoCarritoRequestDTO {

    private Long idCarrito;
    private Long idProducto;
}
