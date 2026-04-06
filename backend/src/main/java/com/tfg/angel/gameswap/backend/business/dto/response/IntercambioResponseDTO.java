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

    private Long idProducto;
    private String nombreProducto;

    private Long idCambio;
    private String nombreCambio;

    private Long idUsuarioProducto;
    private String nombreUsuarioProducto;

    private Long idUsuarioCambio;
    private String nombreUsuarioCambio;

    private LocalDate fecha;
}
