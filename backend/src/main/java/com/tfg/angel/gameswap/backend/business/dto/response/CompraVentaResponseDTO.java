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
    private Long idApiProducto;
    private String nombreProducto;
    private String plataformaProducto;
    private String estadoProducto;
    private Double precio;

    private String descripcion;

    private LocalDate fecha;
}
