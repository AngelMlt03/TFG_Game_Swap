package com.tfg.angel.gameswap.backend.business.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraVentaResponseDTO {

    private Long id;

    private Long idComprador;
    private String nombreComprador;

    private Long idVendedor;
    private String nombreVendedor;

    private Long idProducto;
    private String nombreProducto;

    private Long idPostVenta;
    private Double precio;

    private LocalDate fecha;
}
