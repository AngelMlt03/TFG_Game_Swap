package com.tfg.angel.gameswap.backend.business.mapper;

import com.tfg.angel.gameswap.backend.business.dto.response.CompraVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.CompraVenta;

public class CompraVentaMapper {

    public static CompraVentaResponseDTO toDTO(CompraVenta entity) {
        return CompraVentaResponseDTO.builder()
                .id(entity.getId())

                .idComprador(entity.getComprador().getId())
                .nombreComprador(entity.getComprador().getNombre())

                .idVendedor(entity.getVendedor().getId())
                .nombreVendedor(entity.getVendedor().getNombre())

                .idProducto(entity.getProducto().getId())
                .nombreProducto(entity.getProducto().getNombre())

                .precio(entity.getPrecio())
                .fecha(entity.getFecha())
                .build();
    }
}