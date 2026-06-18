package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.request.ReviewRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.ReviewResponseDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.UsuarioResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.CompraVenta;
import com.tfg.angel.gameswap.backend.business.model.Review;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.Rol;
import com.tfg.angel.gameswap.backend.business.repository.CompraVentaRepository;
import com.tfg.angel.gameswap.backend.business.repository.IntercambioRepository;
import com.tfg.angel.gameswap.backend.business.repository.ReviewRepository;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.UsuarioService;
import com.tfg.angel.gameswap.backend.business.service.impl.ReviewServiceImpl;
import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
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
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private UsuarioDetailsService usuarioDetailsService;
    @Mock
    private CompraVentaRepository compraVentaRepository;
    @Mock
    private IntercambioRepository intercambioRepository;

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

    private Usuario crearUsuario(Long id) {

        Usuario u = new Usuario();

        u.setId(id);
        u.setNombre("Usuario");
        u.setNombreUsuario("usuario" + id);
        u.setCorreo("test@test.com");
        u.setEstrellas(4.0);

        return u;
    }

    private Review crearReview() {

        Usuario reviewer = crearUsuario(1L);
        Usuario reviewed = crearUsuario(2L);

        Review reviewc = new Review();

        reviewc.setId(1L);
        reviewc.setReviewer(reviewer);
        reviewc.setReviewed(reviewed);
        reviewc.setContenido("Buen trato");
        reviewc.setEstrellas(5.0);
        reviewc.setTipoReview("VENTA");

        return reviewc;
    }

    @Test
    void getReviewsRecibidasToUsuario() {

        UsuarioResponseDTO usuario = new UsuarioResponseDTO();
        usuario.setId(2L);

        when(usuarioService.findByUsername("angel"))
                .thenReturn(usuario);

        when(reviewRepository.findByReviewedId(2L))
                .thenReturn(List.of(crearReview()));

        List<ReviewResponseDTO> resultado =
                reviewService.getReviewsRecibidasToUsuario("angel");

        assertEquals(1, resultado.size());
    }

    @Test
    void getReviewsEnviadas() {

        Usuario usuario = crearUsuario(1L);

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        when(reviewRepository.findByReviewerId(1L))
                .thenReturn(List.of(crearReview()));

        List<ReviewResponseDTO> resultado =
                reviewService.getReviewsEnviadas();

        assertEquals(1, resultado.size());
    }

    @Test
    void getByUsuario() {

        Usuario usuario = crearUsuario(2L);

        when(usuarioRepository.findById(2L))
                .thenReturn(Optional.of(usuario));

        when(reviewRepository.findByReviewedId(2L))
                .thenReturn(List.of(crearReview()));

        List<ReviewResponseDTO> resultado =
                reviewService.getByUsuario(2L);

        assertEquals(1, resultado.size());
    }

    @Test
    void actualizarMediaReviews() {

        Usuario usuario = crearUsuario(2L);

        Review r1 = crearReview();
        r1.setEstrellas(4.0);

        Review r2 = crearReview();
        r2.setEstrellas(2.0);

        when(reviewRepository.findByReviewedId(2L))
                .thenReturn(List.of(r1, r2));

        reviewService.actualizarMediaReviews(usuario);

        assertEquals(3.0, usuario.getEstrellas());

        verify(usuarioRepository).save(usuario);
    }

    @Test
    void actualizarMediaReviewsSinReviews() {

        Usuario usuario = crearUsuario(2L);

        when(reviewRepository.findByReviewedId(2L))
                .thenReturn(List.of());

        reviewService.actualizarMediaReviews(usuario);

        assertEquals(0.0, usuario.getEstrellas());

        verify(usuarioRepository).save(usuario);
    }

    @Test
    void delete() {

        Usuario reviewer = crearUsuario(1L);

        Review reviewc = crearReview();

        when(reviewRepository.existsById(1L))
                .thenReturn(true);

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(reviewer);

        when(reviewRepository.findById(1L))
                .thenReturn(Optional.of(reviewc));

        when(reviewRepository.findByReviewedId(2L))
                .thenReturn(List.of());

        reviewService.delete(1L);

        verify(reviewRepository).delete(reviewc);
    }

    @Test
    void deleteUsuarioIncorrecto() {

        Usuario usuarioActual = crearUsuario(99L);

        Review reviewc = crearReview();

        when(reviewRepository.existsById(1L))
                .thenReturn(true);

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuarioActual);

        when(reviewRepository.findById(1L))
                .thenReturn(Optional.of(reviewc));

        assertThrows(
                GSBadRequestException.class,
                () -> reviewService.delete(1L)
        );
    }

    @Test
    void createVentaReview() {

        Usuario reviewer = new Usuario();
        reviewer.setId(1L);
        reviewer.setNombreUsuario("reviewer");

        Usuario reviewed = new Usuario();
        reviewed.setId(2L);
        reviewed.setNombreUsuario("reviewed");

        CompraVenta compra = new CompraVenta();
        compra.setId(10L);

        ReviewRequestDTO dto = new ReviewRequestDTO();
        dto.setIdReviewed(2L);
        dto.setContenido("Muy bien");
        dto.setEstrellas(5.0);
        dto.setTipoReview("VENTA");
        dto.setIdHistorial(10L);

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(reviewer);

        when(usuarioRepository.findById(2L))
                .thenReturn(Optional.of(reviewed));

        when(compraVentaRepository.findById(10L))
                .thenReturn(Optional.of(compra));

        when(reviewRepository.findByReviewedId(2L))
                .thenReturn(List.of());

        when(reviewRepository.save(any(Review.class)))
                .thenAnswer(i -> {
                    Review r = i.getArgument(0);
                    r.setId(1L);
                    return r;
                });

        ReviewResponseDTO resultado = reviewService.create(dto);

        assertNotNull(resultado);

        verify(compraVentaRepository).findById(10L);
    }

}
