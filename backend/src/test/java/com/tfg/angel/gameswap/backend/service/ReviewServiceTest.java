package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.request.ReviewRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.ReviewResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.Review;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.repository.ReviewRepository;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.impl.ReviewServiceImpl;
import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Usuario reviewer;
    private Usuario reviewed;
    private ReviewRequestDTO validDto;

    @BeforeEach
    void setUp() {
        reviewer = Usuario.builder().id(1L).nombre("Angel").build();
        reviewed = Usuario.builder().id(2L).nombre("Juan").build();

        validDto = ReviewRequestDTO.builder()
                .idReviewer(1L)
                .idReviewed(2L)
                .contenido("Buen trato")
                .estrellas(5.0)
                .build();
    }

    @Test
    @DisplayName("Debe crear una review correctamente si los datos son válidos")
    void create_Success() {

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(reviewer));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(reviewed));

        Review reviewGuardada = Review.builder()
                .id(100L)
                .reviewer(reviewer)
                .reviewed(reviewed)
                .contenido("Buen trato")
                .estrellas(5.0)
                .build();

        when(reviewRepository.save(any(Review.class))).thenReturn(reviewGuardada);

        ReviewResponseDTO response = reviewService.create(validDto);

        assertNotNull(response);
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    @DisplayName("Debe lanzar GSBadRequestException si el usuario intenta valorarse a sí mismo")
    void create_ThrowsBadRequest_WhenSelfReview() {

        validDto.setIdReviewed(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(reviewer));

        GSBadRequestException exception = assertThrows(GSBadRequestException.class, () ->
                reviewService.create(validDto)
        );

        assertEquals("Un usuario no puede valorarse a sí mismo", exception.getMessage());
        verify(reviewRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar GSBadRequestException si las estrellas están fuera de rango (6)")
    void create_ThrowsBadRequest_WhenStarsOutOfRange() {

        validDto.setEstrellas(6.0); // Fuera de rango
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(reviewer));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(reviewed));

        assertThrows(GSBadRequestException.class, () -> reviewService.create(validDto));
        verify(reviewRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar GSNotFoundException si el receptor de la review no existe")
    void create_ThrowsNotFound_WhenReviewedMissing() {

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(reviewer));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(GSNotFoundException.class, () -> reviewService.create(validDto));
    }

    @Test
    @DisplayName("Debe buscar una review por ID")
    void findById_Success() {

        Review review = Review.builder().id(1L).reviewer(reviewer).reviewed(reviewed).build();
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        ReviewResponseDTO response = reviewService.findById(1L);

        assertNotNull(response);
        verify(reviewRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe eliminar una review correctamente si existe")
    void delete_Success() {

        when(reviewRepository.existsById(1L)).thenReturn(true);

        reviewService.delete(1L);

        verify(reviewRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Debe lanzar GSNotFoundException al borrar una review que no existe")
    void delete_ThrowsNotFound() {

        when(reviewRepository.existsById(1L)).thenReturn(false);

        assertThrows(GSNotFoundException.class, () -> reviewService.delete(1L));
        verify(reviewRepository, never()).deleteById(anyLong());
    }
}
