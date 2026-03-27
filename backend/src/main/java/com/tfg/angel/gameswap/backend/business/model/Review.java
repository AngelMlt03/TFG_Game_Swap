package com.tfg.angel.gameswap.backend.business.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reviewer")
    private Usuario reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reviewed")
    private Usuario reviewed;

    @Column(columnDefinition = "TEXT")
    private String contenido;

    private Double estrellas;
}
