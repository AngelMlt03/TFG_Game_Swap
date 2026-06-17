package com.tfg.angel.gameswap.backend.business.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostIntercambioResponseDTO {

    private Long id;

    private Long idUsuario;
    private String nombreUsuario;

    private Long idProducto;
    private Long idApi;
    private String nombreProducto;
    private String estado;
    private String plataforma;

    private Long idProductoIntercambio;
    private Long idApiProductoIntercambio;
    private String nombreProductoIntercambio;
    private String estadoIntercambio;
    private String plataformaIntercambio;

    private String descripcion;
}
