package com.tfg.angel.gameswap.backend.business.mapper;

import com.tfg.angel.gameswap.backend.business.dto.request.ProductoRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.ProductoResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.Producto;

public class ProductoMapper {
    private ProductoMapper() { }

    public static Producto toEntity(ProductoRequestDTO dto) {
        return Producto.builder()
                .idAPI(dto.getIdAPI())
                .nombre(dto.getNombre())
                .estado(dto.getEstado())
                .build();
    }

    public static ProductoResponseDTO toDTO(Producto producto) {
        return ProductoResponseDTO.builder()
                .id(producto.getId())
                .idAPI(producto.getIdAPI())
                .nombre(producto.getNombre())
                .estado(producto.getEstado())
                .build();
    }
}
