package com.tfg.angel.gameswap.backend.business.mapper;

import com.tfg.angel.gameswap.backend.business.dto.response.PostIntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.PostIntercambio;

public class PostIntercambioMapper {
    private PostIntercambioMapper() { }

    public static PostIntercambioResponseDTO toDTO(PostIntercambio post) {
        return PostIntercambioResponseDTO.builder()
                .id(post.getId())

                .idUsuario(post.getUsuario().getId())
                .nombreUsuario(post.getUsuario().getNombreUsuario())

                .idProducto(post.getProducto().getId())
                .idApi(post.getProducto().getIdAPI().longValue())
                .nombreProducto(post.getProducto().getNombre())
                .plataforma(post.getPlataforma())
                .estado(post.getProducto().getEstado().toString())

                .idProductoIntercambio(post.getProductoCambio().getId())
                .idApiProductoIntercambio(post.getProductoCambio().getIdAPI().longValue())
                .nombreProductoIntercambio(post.getProductoCambio().getNombre())
                .plataformaIntercambio(post.getPlataformaCambio())
                .estadoIntercambio(post.getProductoCambio().getEstado().toString())

                .descripcion(post.getDescripcion())

                .build();
    }
}
