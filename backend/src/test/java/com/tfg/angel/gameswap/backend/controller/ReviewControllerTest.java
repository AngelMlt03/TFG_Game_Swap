package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.controller.ReviewController;
import com.tfg.angel.gameswap.backend.business.dto.request.ReviewRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.CompraVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.IntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.ReviewResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.ReviewService;
import com.tfg.angel.gameswap.backend.business.service.impl.HistorialService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private ReviewService service;

    @Mock
    private HistorialService historialService;

    @InjectMocks
    private ReviewController controller;

    private ReviewResponseDTO crearReview() {
        return ReviewResponseDTO.builder()
                .id(1L)
                .idReviewer(1L)
                .nombreReviewer("Angel")
                .idReviewed(2L)
                .nombreReviewed("Pepe")
                .contenido("Muy buen trato")
                .estrellas(5.0)
                .tipoReview("COMPRA")
                .idCompraVenta(1L)
                .build();
    }

    @Test
    void create_debeCrearReview() {

        ReviewRequestDTO dto = new ReviewRequestDTO();

        ReviewResponseDTO esperado = crearReview();

        when(service.create(dto)).thenReturn(esperado);

        ReviewResponseDTO resultado = controller.create(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(service).create(dto);
    }

    @Test
    void findById_debeDevolverReview() {

        Long id = 1L;

        ReviewResponseDTO esperado = crearReview();

        when(service.findById(id)).thenReturn(esperado);

        ReviewResponseDTO resultado = controller.findById(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());

        verify(service).findById(id);
    }

    @Test
    void findAll_debeDevolverLista() {

        List<ReviewResponseDTO> esperado =
                List.of(crearReview());

        when(service.findAll()).thenReturn(esperado);

        List<ReviewResponseDTO> resultado = controller.findAll();

        assertEquals(1, resultado.size());

        verify(service).findAll();
    }

    @Test
    void findByReviewed_debeBuscarPorReviewed() {

        Long id = 2L;

        List<ReviewResponseDTO> esperado =
                List.of(crearReview());

        when(service.findByReviewed(id)).thenReturn(esperado);

        List<ReviewResponseDTO> resultado =
                controller.findByReviewed(id);

        assertEquals(1, resultado.size());

        verify(service).findByReviewed(id);
    }

    @Test
    void findByReviewer_debeBuscarPorReviewer() {

        Long id = 1L;

        List<ReviewResponseDTO> esperado =
                List.of(crearReview());

        when(service.findByReviewer(id)).thenReturn(esperado);

        List<ReviewResponseDTO> resultado =
                controller.findByReviewer(id);

        assertEquals(1, resultado.size());

        verify(service).findByReviewer(id);
    }

    @Test
    void getMisReviews_debeDevolverReviewsRecibidas() {

        String usuario = "Angel";

        List<ReviewResponseDTO> esperado =
                List.of(crearReview());

        when(service.getReviewsRecibidasToUsuario(usuario))
                .thenReturn(esperado);

        List<ReviewResponseDTO> resultado =
                controller.getMisReviews(usuario);

        assertEquals(1, resultado.size());

        verify(service).getReviewsRecibidasToUsuario(usuario);
    }

    @Test
    void getReviewsEnviadas_debeDevolverReviewsEnviadas() {

        List<ReviewResponseDTO> esperado =
                List.of(crearReview());

        when(service.getReviewsEnviadas())
                .thenReturn(esperado);

        List<ReviewResponseDTO> resultado =
                controller.getReviewsEnviadas();

        assertEquals(1, resultado.size());

        verify(service).getReviewsEnviadas();
    }

    @Test
    void getCompra_debeDevolverCompra() {

        Long id = 1L;

        CompraVentaResponseDTO compra =
                CompraVentaResponseDTO.builder()
                        .id(id)
                        .build();

        when(historialService.getCompra(id))
                .thenReturn(compra);

        CompraVentaResponseDTO resultado =
                controller.getCompra(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());

        verify(historialService).getCompra(id);
    }

    @Test
    void getIntercambio_debeDevolverIntercambio() {

        Long id = 1L;

        IntercambioResponseDTO intercambio =
                IntercambioResponseDTO.builder()
                        .id(id)
                        .build();

        when(historialService.getIntercambio(id))
                .thenReturn(intercambio);

        IntercambioResponseDTO resultado =
                controller.getIntercambio(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());

        verify(historialService).getIntercambio(id);
    }

    @Test
    void getByUsuario_debeBuscarPorUsuario() {

        Long idUsuario = 1L;

        List<ReviewResponseDTO> esperado =
                List.of(crearReview());

        when(service.getByUsuario(idUsuario))
                .thenReturn(esperado);

        List<ReviewResponseDTO> resultado =
                controller.getByUsuario(idUsuario);

        assertEquals(1, resultado.size());

        verify(service).getByUsuario(idUsuario);
    }

    @Test
    void delete_debeEliminarReview() {

        Long id = 1L;

        controller.delete(id);

        verify(service).delete(id);
    }
}