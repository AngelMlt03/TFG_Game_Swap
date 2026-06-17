package com.tfg.angel.gameswap.backend.business.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class GameDetailsDto {

    private String nombre;
    private String resumen;

    private Double rating;

    private List<String> generos;
    private List<String> temas;
    private List<String> modos;

    private String trailerUrl;

    private String cover;

    private List<String> screenshots;

    private Long releaseDate;
}