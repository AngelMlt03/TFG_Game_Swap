package com.tfg.angel.gameswap.backend.business.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarritoResponseDTO {

    private Long id;

    private Long idUsuario;
    private String nombreUsuario;

    private Double coste;

    private List<ProductoCarritoResponseDTO> productos;
}
