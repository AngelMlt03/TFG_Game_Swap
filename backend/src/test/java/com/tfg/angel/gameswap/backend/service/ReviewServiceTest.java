package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.response.ReviewResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.Review;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.Rol;
import com.tfg.angel.gameswap.backend.business.repository.ReviewRepository;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.impl.ReviewServiceImpl;
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

    @BeforeEach
    void setUp() {
        Usuario reviewer = Usuario.builder().id(1L).nombre("Angel").rol(Rol.CLIENTE).build();
        Usuario reviewed = Usuario.builder().id(2L).nombre("Juan").rol(Rol.CLIENTE).build();

        review = Review.builder()
                .id(100L)
                .reviewer(reviewer)
                .reviewed(reviewed)
                .contenido("Buena experiencia")
                .estrellas(5.0)
                .build();
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

    private Usuario reviewer() {
        return Usuario.builder()
                .id(1L)
                .nombre("Reviewer")
                .build();
    }

    private Usuario reviewed() {
        return Usuario.builder()
                .id(2L)
                .nombre("Reviewed")
                .build();
    }

    private Review review() {
        return Review.builder()
                .id(1L)
                .reviewer(reviewer())
                .reviewed(reviewed())
                .contenido("Muy bien")
                .estrellas(5.0)
                .tipoReview("VENTA")
                .build();
    }

    @Test
    void findById_ok() {

        when(reviewRepository.findById(1L))
                .thenReturn(Optional.of(review()));

        assertNotNull(reviewService.findById(1L));
    }

    @Test
    void findById_notFound() {

        when(reviewRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                GSNotFoundException.class,
                () -> reviewService.findById(1L)
        );
    }

    @Test
    void findAll_ok() {

        when(reviewRepository.findAll())
                .thenReturn(List.of(review()));

        assertEquals(1, reviewService.findAll().size());
    }

    @Test
    void findByReviewed_ok() {

        when(reviewRepository.findByReviewedId(2L))
                .thenReturn(List.of(review()));

        assertEquals(1, reviewService.findByReviewed(2L).size());
    }

    @Test
    void findByReviewer_ok() {

        when(reviewRepository.findByReviewerId(1L))
                .thenReturn(List.of(review()));

        assertEquals(1, reviewService.findByReviewer(1L).size());
    }


    @Test
    void getByUsuario_ok() {

        when(usuarioRepository.findById(2L))
                .thenReturn(Optional.of(reviewed()));

        when(reviewRepository.findByReviewedId(2L))
                .thenReturn(List.of(review()));

        assertEquals(
                1,
                reviewService.getByUsuario(2L).size()
        );
    }

    @Test
    void actualizarMediaReviews_ok() {

        when(reviewRepository.findByReviewedId(2L))
                .thenReturn(List.of(
                        review(),
                        Review.builder()
                                .estrellas(3.0)
                                .build()
                ));

        reviewService.actualizarMediaReviews(reviewed());

        verify(usuarioRepository).save(any());
    }

    @Test
    void actualizarMediaReviews_emptyReviews() {

        when(reviewRepository.findByReviewedId(2L))
                .thenReturn(List.of());

        reviewService.actualizarMediaReviews(reviewed());

        verify(usuarioRepository).save(any());
    }

    @Test
    void delete_notFound() {

        when(reviewRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(
                GSNotFoundException.class,
                () -> reviewService.delete(1L)
        );
    }

}
