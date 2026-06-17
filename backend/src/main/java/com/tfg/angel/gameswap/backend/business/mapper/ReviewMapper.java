package com.tfg.angel.gameswap.backend.business.mapper;

import com.tfg.angel.gameswap.backend.business.dto.response.ReviewResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.Review;

public class ReviewMapper {
    private ReviewMapper() { }

    public static ReviewResponseDTO toDTO(Review review) {
        return ReviewResponseDTO.builder()
                .id(review.getId())

                .idReviewer(review.getReviewer().getId())
                .nombreReviewer(review.getReviewer().getNombreUsuario())

                .idReviewed(review.getReviewed().getId())
                .nombreReviewed(review.getReviewed().getNombreUsuario())

                .contenido(review.getContenido())
                .estrellas(review.getEstrellas())
                .tipoReview(review.getTipoReview())

                .idCompraVenta( review.getCompraVenta() != null
                                ? review.getCompraVenta().getId()
                                : null )

                .idIntercambio( review.getIntercambio() != null
                                ? review.getIntercambio().getId()
                                : null )

                .build();
    }
}
