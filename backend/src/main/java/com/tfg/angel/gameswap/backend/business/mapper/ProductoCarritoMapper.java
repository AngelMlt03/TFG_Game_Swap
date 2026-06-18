package com.tfg.angel.gameswap.backend.business.mapper;

import com.tfg.angel.gameswap.backend.business.dto.response.ProductoCarritoResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.PostVenta;
import com.tfg.angel.gameswap.backend.business.model.ProductoCarrito;

public class ProductoCarritoMapper {

    public static ProductoCarritoResponseDTO toDTO(ProductoCarrito pc) {

        PostVenta pv = pc.getPostVenta();

        return ProductoCarritoResponseDTO.builder()

                .id(pc.getId())
                .idPostVenta(pv.getId())
                .nombreProducto(pv.getProducto().getNombre())

                .plataforma(pv.getPlataforma())

                .estado(pv.getProducto().getEstado().name())

                .precio(pv.getPrecio())

                .idApi(Long.valueOf(pv.getProducto().getIdAPI()))

                .build();
    }
}
