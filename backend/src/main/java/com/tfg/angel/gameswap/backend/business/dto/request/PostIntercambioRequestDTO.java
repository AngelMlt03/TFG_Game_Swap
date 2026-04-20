package com.tfg.angel.gameswap.backend.business.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostIntercambioRequestDTO {

    private Long idUsuario;
    private Long idProducto;
    private Long idProductoCambio;
}
