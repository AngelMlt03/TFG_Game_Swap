package com.tfg.angel.gameswap.backend.business.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntercambioResponseDTO {

    private Long id;

    private Long idPostIntercambio;

    private Long idUsuarioPublicador;
    private String nombreUsuarioPublicador;

    private Long idProductoOfrecido;
    private String nombreProductoOfrecido;

    private Long idProductoDeseado;
    private String nombreProductoDeseado;

    private Long idUsuarioCambio;
    private String nombreUsuarioCambio;

    private LocalDate fecha;
}
