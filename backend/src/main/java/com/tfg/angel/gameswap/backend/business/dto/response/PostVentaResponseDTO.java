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
    private Long idApi;
    private String nombreProducto;
    private String plataforma;
    private Double precio;
    private String descripcion;
}
