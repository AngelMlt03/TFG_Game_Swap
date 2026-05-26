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
    private Long idApiProducto;
    private String nombreProducto;
    private String estado;
    private String plataforma;

    private Long idProductoCambio;
    private Long idApiProductoCambio;
    private String nombreProductoCambio;
    private String estadoCambio;
    private String plataformaCambio;

    private String descripcion;
}
