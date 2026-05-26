package com.tfg.angel.gameswap.backend.business.mapper;

import com.tfg.angel.gameswap.backend.business.dto.response.CarritoResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.Carrito;

import java.util.stream.Collectors;

public class CarritoMapper {
    private CarritoMapper() { }

    public static CarritoResponseDTO toDTO(Carrito entity) {
        return CarritoResponseDTO.builder()
                .id(entity.getId())
                .idUsuario(entity.getUsuario().getId())
                .nombreUsuario(entity.getUsuario().getNombre())
                .coste(entity.getCoste())
                .productos(entity.getProductos()
                        .stream()
                        .map(ProductoCarritoMapper::toDTO)
                        .collect(Collectors.toList()))
                .build();
    }
}
