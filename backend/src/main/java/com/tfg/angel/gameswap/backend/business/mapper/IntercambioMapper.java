package com.tfg.angel.gameswap.backend.business.mapper;

import com.tfg.angel.gameswap.backend.business.dto.response.IntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.Intercambio;

public class IntercambioMapper {
    private IntercambioMapper() { }

    public static IntercambioResponseDTO toDTO(Intercambio i) {

        return IntercambioResponseDTO.builder()
                .id(i.getId())

                .idPostIntercambio(i.getPostIntercambio().getId())

                .idUsuarioPublicador(i.getPostIntercambio().getUsuario().getId())
                .nombreUsuarioPublicador(i.getPostIntercambio().getUsuario().getNombre())

                .idUsuarioCambio(i.getUsuarioCambio().getId())
                .nombreUsuarioCambio(i.getUsuarioCambio().getNombre())

                .idProductoOfrecido(i.getPostIntercambio().getProducto().getId())
                .idApiProductoOfrecido(i.getPostIntercambio().getProducto().getIdAPI().longValue())
                .nombreProductoOfrecido(i.getPostIntercambio().getProducto().getNombre())
                .plataformaProductoOfrecido(i.getPostIntercambio().getPlataforma())
                .estadoProductoOfrecido(i.getPostIntercambio().getProducto().getEstado().toString())

                .idProductoDeseado(i.getPostIntercambio().getProductoCambio().getId())
                .idApiProductoDeseado(i.getPostIntercambio().getProductoCambio().getIdAPI().longValue())
                .nombreProductoDeseado(i.getPostIntercambio().getProductoCambio().getNombre())
                .plataformaProductoDeseado(i.getPostIntercambio().getPlataformaCambio())
                .estadoProductoDeseado(i.getPostIntercambio().getProductoCambio().getEstado().toString())

                .fecha(i.getFecha())

                .fecha(i.getFecha())
                .build();
    }
}
