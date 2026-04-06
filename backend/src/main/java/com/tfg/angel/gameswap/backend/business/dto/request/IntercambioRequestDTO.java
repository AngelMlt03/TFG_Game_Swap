package com.tfg.angel.gameswap.backend.business.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntercambioRequestDTO {

    private Long idProducto;
    private Long idCambio;

    private Long idUsuarioProducto;
    private Long idUsuarioCambio;
}
