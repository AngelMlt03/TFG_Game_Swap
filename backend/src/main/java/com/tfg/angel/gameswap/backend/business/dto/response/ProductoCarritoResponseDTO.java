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

    private String nombreProducto;

    private String plataforma;

    private String estado;

    private Double precio;

    private Long idApi;
}
