package com.tfg.angel.gameswap.backend.business.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostIntercambioRequestDTO {

    // PRODUCTO PROPIO
    private String nombreProducto;
    private String plataforma;
    private String estadoProducto;
    private Long idApi;

    // PRODUCTO BUSCADO
    private String nombreProductoIntercambio;
    private String plataformaIntercambio;
    private String estadoProductoIntercambio;
    private Long idApiIntercambio;

    private String descripcion;
}
