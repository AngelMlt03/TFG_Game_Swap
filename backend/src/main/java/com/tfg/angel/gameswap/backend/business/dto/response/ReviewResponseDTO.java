package com.tfg.angel.gameswap.backend.business.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDTO {

    private Long id;

    private Long idReviewer;
    private String nombreReviewer;

    private Long idReviewed;
    private String nombreReviewed;

    private String contenido;

    private Double estrellas;

    private String tipoReview;

    private Long idCompraVenta;
    private Long idIntercambio;
}
