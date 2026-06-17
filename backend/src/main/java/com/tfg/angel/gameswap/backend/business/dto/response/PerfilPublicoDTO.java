package com.tfg.angel.gameswap.backend.business.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PerfilPublicoDTO {

    private String nombre;
    private String nombreUsuario;
    private String correo;
    private Double estrellas;
}
