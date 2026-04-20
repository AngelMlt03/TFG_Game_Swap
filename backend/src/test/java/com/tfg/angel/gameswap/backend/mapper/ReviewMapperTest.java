package com.tfg.angel.gameswap.backend.mapper;

import com.tfg.angel.gameswap.backend.business.mapper.ReviewMapper;
import com.tfg.angel.gameswap.backend.business.model.Review;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewMapperTest {

    @Test
    void toDTO_ok() {

        Usuario usuario1 = Usuario.builder().id(1L).build();
        Usuario usuario2 = Usuario.builder().id(2L).build();

        Review review = Review.builder()
                .id(1L)
                .reviewer(usuario1)
                .reviewed(usuario2)
                .contenido("ok")
                .estrellas(4.0)
                .build();

        var dto = ReviewMapper.toDTO(review);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(usuario1.getId(), dto.getIdReviewer());
        assertEquals(usuario2.getId(), dto.getIdReviewed());
        assertEquals("ok", dto.getContenido());
        assertEquals(4.0, dto.getEstrellas());
    }
}
