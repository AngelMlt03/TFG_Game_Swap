package com.tfg.angel.gameswap.backend.business.mapper;

import com.tfg.angel.gameswap.backend.business.dto.response.IntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.Intercambio;

public class IntercambioMapper {

    public static IntercambioResponseDTO toDTO(Intercambio entity) {
        return IntercambioResponseDTO.builder()
                .id(entity.getId())

                .idProducto(entity.getProducto().getId())
                .nombreProducto(entity.getProducto().getNombre())

                .idCambio(entity.getProductoCambio().getId())
                .nombreCambio(entity.getProductoCambio().getNombre())

                .idUsuarioProducto(entity.getUsuarioProducto().getId())
                .nombreUsuarioProducto(entity.getUsuarioProducto().getNombre())

                .idUsuarioCambio(entity.getUsuarioCambio().getId())
                .nombreUsuarioCambio(entity.getUsuarioCambio().getNombre())

                .fecha(entity.getFecha())
                .build();
    }
}
