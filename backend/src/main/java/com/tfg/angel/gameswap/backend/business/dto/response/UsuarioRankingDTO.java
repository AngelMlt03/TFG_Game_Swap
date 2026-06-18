package com.tfg.angel.gameswap.backend.business.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRankingDTO {

    private String nombreUsuario;
    private Double estrellas;
}
