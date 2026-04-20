package com.tfg.angel.gameswap.backend.business.mapper;

import com.tfg.angel.gameswap.backend.business.dto.response.ReviewResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.Review;

public class ReviewMapper {

    public static ReviewResponseDTO toDTO(Review entity) {
        return ReviewResponseDTO.builder()
                .id(entity.getId())
                .idReviewer(entity.getReviewer().getId())
                .nombreReviewer(entity.getReviewer().getNombre())
                .idReviewed(entity.getReviewed().getId())
                .nombreReviewed(entity.getReviewed().getNombre())
                .contenido(entity.getContenido())
                .estrellas(entity.getEstrellas())
                .build();
    }
}
