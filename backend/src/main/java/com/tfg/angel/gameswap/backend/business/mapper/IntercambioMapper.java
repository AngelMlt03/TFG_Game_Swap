package com.tfg.angel.gameswap.backend.business.mapper;

import com.tfg.angel.gameswap.backend.business.dto.response.IntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.Intercambio;

public class IntercambioMapper {

    public static IntercambioResponseDTO toDTO(Intercambio entity) {

        return IntercambioResponseDTO.builder()
                .id(entity.getId())

                .idPostIntercambio(entity.getPostIntercambio().getId())

                .idUsuarioPublicador(entity.getPostIntercambio().getUsuario().getId())
                .nombreUsuarioPublicador(entity.getPostIntercambio().getUsuario().getNombre())

                .idProductoOfrecido(entity.getPostIntercambio().getProducto().getId())
                .nombreProductoOfrecido(entity.getPostIntercambio().getProducto().getNombre())

                .idProductoDeseado(entity.getPostIntercambio().getProductoCambio().getId())
                .nombreProductoDeseado(entity.getPostIntercambio().getProductoCambio().getNombre())

                .idUsuarioCambio(entity.getUsuarioCambio().getId())
                .nombreUsuarioCambio(entity.getUsuarioCambio().getNombre())

                .fecha(entity.getFecha())
                .build();
    }
}
