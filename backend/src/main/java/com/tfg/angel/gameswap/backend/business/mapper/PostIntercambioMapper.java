package com.tfg.angel.gameswap.backend.business.mapper;

import com.tfg.angel.gameswap.backend.business.dto.response.PostIntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.PostIntercambio;

public class PostIntercambioMapper {

    public static PostIntercambioResponseDTO toDTO(PostIntercambio post) {
        return PostIntercambioResponseDTO.builder()
                .id(post.getId())
                .idUsuario(post.getUsuario().getId())
                .nombreUsuario(post.getUsuario().getNombre())
                .idProducto(post.getProducto().getId())
                .nombreProducto(post.getProducto().getNombre())
                .idProductoCambio(post.getProductoCambio().getId())
                .nombreProductoCambio(post.getProductoCambio().getNombre())
                .build();
    }
}
