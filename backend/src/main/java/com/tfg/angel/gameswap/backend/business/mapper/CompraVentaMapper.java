package com.tfg.angel.gameswap.backend.business.mapper;

import com.tfg.angel.gameswap.backend.business.dto.response.CompraVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.CompraVenta;

public class CompraVentaMapper {
    private CompraVentaMapper() { }


    public static CompraVentaResponseDTO toDTO(CompraVenta c) {
        return CompraVentaResponseDTO.builder()
                .id(c.getId())

                .idComprador(c.getComprador().getId())
                .nombreComprador(c.getComprador().getNombre())

                .idVendedor(c.getPostVenta().getVendedor().getId())
                .nombreVendedor(c.getPostVenta().getVendedor().getNombre())

                .idProducto(c.getPostVenta().getProducto().getId())
                .idApiProducto(c.getPostVenta().getProducto().getIdAPI().longValue())
                .nombreProducto(c.getPostVenta().getProducto().getNombre())
                .plataformaProducto(c.getPostVenta().getPlataforma())
                .estadoProducto(c.getPostVenta().getProducto().getEstado().toString())
                .precio(c.getPrecio())

                .descripcion(c.getPostVenta().getDescripcion())

                .fecha(c.getFecha())
                .build();
    }
}