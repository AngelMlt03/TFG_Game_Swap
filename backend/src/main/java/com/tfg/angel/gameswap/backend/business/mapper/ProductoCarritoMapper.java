package com.tfg.angel.gameswap.backend.business.mapper;

import com.tfg.angel.gameswap.backend.business.dto.response.ProductoCarritoResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.ProductoCarrito;

public class ProductoCarritoMapper {

    public static ProductoCarritoResponseDTO toDTO(ProductoCarrito entity) {

        return ProductoCarritoResponseDTO.builder()
                .id(entity.getId())
                .idPostVenta(entity.getPostVenta().getId())
                .precio(entity.getPostVenta().getPrecio())
                .idProducto(entity.getPostVenta().getProducto().getId())
                .nombreProducto(entity.getPostVenta().getProducto().getNombre())
                .idVendedor(entity.getPostVenta().getVendedor().getId())
                .nombreVendedor(entity.getPostVenta().getVendedor().getNombre())
                .build();
    }
}
