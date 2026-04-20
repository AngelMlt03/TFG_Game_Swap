package com.tfg.angel.gameswap.backend.business.mapper;

import com.tfg.angel.gameswap.backend.business.dto.response.PostVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.PostVenta;

public class PostVentaMapper {

    public static PostVentaResponseDTO toDTO(PostVenta post) {
        return PostVentaResponseDTO.builder()
                .id(post.getId())
                .idVendedor(post.getVendedor().getId())
                .nombreVendedor(post.getVendedor().getNombre())
                .idProducto(post.getProducto().getId())
                .nombreProducto(post.getProducto().getNombre())
                .precio(post.getPrecio())
                .build();
    }
}
