package com.tfg.angel.gameswap.backend.business.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestDTO {

    private Long idReviewer;
    private Long idReviewed;
    private String contenido;
    private Double estrellas;

    private String tipoReview;
    private Long idHistorial;
}
