package com.tfg.angel.gameswap.backend.business.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoCarritoResponseDTO {

    private Long id;

    private Long idPostVenta;
    private Double precio;

    private Long idProducto;
    private String nombreProducto;

    private Long idVendedor;
    private String nombreVendedor;
}
