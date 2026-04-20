package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.request.ReviewRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.ReviewResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.Review;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.Rol;
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

import java.util.List;
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

    private Review review;
    private Usuario reviewer;
    private Usuario reviewed;
    private ReviewRequestDTO validDto;

    @BeforeEach
    void setUp() {
        reviewer = Usuario.builder().id(1L).nombre("Angel").rol(Rol.CLIENTE).build();
        reviewed = Usuario.builder().id(2L).nombre("Juan").rol(Rol.CLIENTE).build();

        validDto = ReviewRequestDTO.builder()
                .idReviewer(1L)
                .idReviewed(2L)
                .contenido("Buen trato")
                .estrellas(5.0)
                .build();

        review = Review.builder()
                .id(100L)
                .reviewer(reviewer)
                .reviewed(reviewed)
                .contenido("Buena experiencia")
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

    @Test
    @DisplayName("Debe retornar el DTO si la review existe")
    void findById_Success() {

        when(reviewRepository.findById(100L)).thenReturn(Optional.of(review));

        ReviewResponseDTO result = reviewService.findById(100L);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        verify(reviewRepository).findById(100L);
    }

    @Test
    @DisplayName("Debe lanzar GSNotFoundException si la review no existe")
    void findById_NotFound() {

        when(reviewRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(GSNotFoundException.class, () -> reviewService.findById(999L));
    }

    @Test
    @DisplayName("Debe retornar la lista completa de reviews")
    void findAll_Success() {

        when(reviewRepository.findAll()).thenReturn(List.of(review, review));

        List<ReviewResponseDTO> result = reviewService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reviewRepository).findAll();
    }

    @Test
    @DisplayName("Debe retornar las reviews recibidas por un usuario")
    void findByReviewed_Success() {

        Long idReviewed = 2L;
        when(reviewRepository.findByReviewedId(idReviewed)).thenReturn(List.of(review));

        List<ReviewResponseDTO> result = reviewService.findByReviewed(idReviewed);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(reviewRepository).findByReviewedId(idReviewed);
    }

    @Test
    @DisplayName("Debe retornar las reviews realizadas por un usuario")
    void findByReviewer_Success() {

        Long idReviewer = 1L;
        when(reviewRepository.findByReviewerId(idReviewer)).thenReturn(List.of(review));

        List<ReviewResponseDTO> result = reviewService.findByReviewer(idReviewer);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(reviewRepository).findByReviewerId(idReviewer);
    }
}
