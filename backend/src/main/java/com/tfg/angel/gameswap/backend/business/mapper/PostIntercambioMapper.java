package com.tfg.angel.gameswap.backend.business.mapper;

import com.tfg.angel.gameswap.backend.business.dto.response.PostIntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.PostIntercambio;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;

public class PostIntercambioMapper {
    private PostIntercambioMapper() { }

    public static PostIntercambioResponseDTO toDTO(PostIntercambio post) {
        return PostIntercambioResponseDTO.builder()
                .id(post.getId())

                .idUsuario(post.getUsuario().getId())
                .nombreUsuario(post.getUsuario().getNombre())

                .idProducto(post.getProducto().getId())
                .idApiProducto(post.getProducto().getIdAPI().longValue())
                .nombreProducto(post.getProducto().getNombre())
                .plataforma(post.getPlataforma())
                .estado(post.getProducto().getEstado().toString())

                .idProductoCambio(post.getProductoCambio().getId())
                .idApiProductoCambio(post.getProductoCambio().getIdAPI().longValue())
                .nombreProductoCambio(post.getProductoCambio().getNombre())
                .plataformaCambio(post.getPlataformaCambio())
                .estadoCambio(post.getProductoCambio().getEstado().toString())

                .descripcion(post.getDescripcion())

                .build();
    }
}
