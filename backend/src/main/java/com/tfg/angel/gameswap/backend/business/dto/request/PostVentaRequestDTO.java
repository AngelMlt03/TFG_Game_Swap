package com.tfg.angel.gameswap.backend.business.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostVentaRequestDTO {

    private Long idProducto;
    private Long idApi;
    private String nombreProducto;
    private String plataforma;
    private String estadoProducto;

    private Double precio;
    private String descripcion;
}
