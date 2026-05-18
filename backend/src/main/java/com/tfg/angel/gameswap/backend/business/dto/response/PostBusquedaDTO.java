package com.tfg.angel.gameswap.backend.business.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostBusquedaDTO {

    private Long id;
    private String tipo;

    // Juego principal
    private Long idApi;
    private String nombreProducto;
    private String plataforma;
    private String estado;

    // Venta
    private Double precio;

    // Intercambio
    private Long idApiIntercambio;
    private String nombreProductoIntercambio;
    private String plataformaIntercambio;
    private String estadoIntercambio;

    // Usuario
    private String nombreUsuario;

    // Extra
    private String descripcion;
}