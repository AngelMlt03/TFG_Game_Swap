package com.tfg.angel.gameswap.backend.business.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeStatsDTO {

    private Long ventas;
    private Long intercambios;
    private Long usuarios;
    private Long reviews;
}
