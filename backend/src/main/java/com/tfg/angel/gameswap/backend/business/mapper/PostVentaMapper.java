package com.tfg.angel.gameswap.backend.business.mapper;

import com.tfg.angel.gameswap.backend.business.dto.response.PostVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.PostVenta;

public class PostVentaMapper {
    private PostVentaMapper() { }

    public static PostVentaResponseDTO toDTO(PostVenta post) {
        return PostVentaResponseDTO.builder()
                .id(post.getId())
                .idVendedor(post.getVendedor().getId())
                .nombreVendedor(post.getVendedor().getNombre())
                .plataforma(post.getPlataforma())
                .idProducto(post.getProducto().getId())
                .idApi(post.getProducto().getIdAPI().longValue())
                .nombreProducto(post.getProducto().getNombre())
                .precio(post.getPrecio())

                .descripcion(post.getDescripcion())
                .build();
    }
}
