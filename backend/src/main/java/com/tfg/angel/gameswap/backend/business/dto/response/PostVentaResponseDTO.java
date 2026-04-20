package com.tfg.angel.gameswap.backend.business.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostVentaResponseDTO {

    private Long id;
    private Long idVendedor;
    private String nombreVendedor;
    private Long idProducto;
    private String nombreProducto;
    private Double precio;
}
