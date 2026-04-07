package com.tfg.angel.gameswap.backend.business.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Intercambio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_post_intercambio")
    private PostIntercambio postIntercambio;

    @ManyToOne
    @JoinColumn(name = "id_usuario_cambio")
    private Usuario usuarioCambio;

    private LocalDate fecha;
}
