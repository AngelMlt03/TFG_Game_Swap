package com.tfg.angel.gameswap.backend.business.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraVentaRequestDTO {

    private Long idComprador;
    private Long idVendedor;
    private Long idProducto;
    private Double precio;
}
