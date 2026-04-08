package com.tfg.angel.gameswap.backend.business.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostVentaRequestDTO {

    private Long idVendedor;
    private Long idProducto;
    private Double precio;
}
