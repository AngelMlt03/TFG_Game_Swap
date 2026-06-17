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

    private Long idUsuarioCambio;
    private String nombreUsuarioCambio;

    private Long idProductoOfrecido;
    private Long idApiProductoOfrecido;
    private String nombreProductoOfrecido;
    private String plataformaProductoOfrecido;
    private String estadoProductoOfrecido;

    private Long idProductoDeseado;
    private Long idApiProductoDeseado;
    private String nombreProductoDeseado;
    private String plataformaProductoDeseado;
    private String estadoProductoDeseado;

    private String descripcion;

    private LocalDate fecha;
}
