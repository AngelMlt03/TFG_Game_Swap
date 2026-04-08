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
    private String nombreProducto;

    private Long idProductoCambio;
    private String nombreProductoCambio;
}
