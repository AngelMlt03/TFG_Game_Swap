package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.controller.PostVentaController;
import com.tfg.angel.gameswap.backend.business.dto.request.PostIntercambioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.request.PostVentaRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import com.tfg.angel.gameswap.backend.business.service.PostVentaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostVentaControllerTest {

    @Mock
    private PostVentaService postVentaService;

    @InjectMocks
    private PostVentaController controller;

    private PostVentaResponseDTO crearResponse() {
        return PostVentaResponseDTO.builder()
                .id(1L)
                .idVendedor(10L)
                .nombreUsuario("angel")
                .idProducto(100L)
                .idApi(200L)
                .nombreProducto("Pokemon")
                .plataforma("Nintendo Switch")
                .precio(49.99)
                .descripcion("Juego en perfecto estado")
                .build();
    }

    @Test
    void create_debeCrearPostVenta() {

        PostVentaRequestDTO dto = new PostVentaRequestDTO();

        PostVentaResponseDTO esperado = crearResponse();

        when(postVentaService.create(dto)).thenReturn(esperado);

        PostVentaResponseDTO resultado = controller.create(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(postVentaService).create(dto);
    }

    @Test
    void findById_debeDevolverPostVenta() {

        Long id = 1L;

        PostVentaResponseDTO esperado = crearResponse();

        when(postVentaService.findById(id)).thenReturn(esperado);

        PostVentaResponseDTO resultado = controller.findById(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());

        verify(postVentaService).findById(id);
    }

    @Test
    void findAll_debeDevolverLista() {

        List<PostVentaResponseDTO> esperado =
                List.of(crearResponse());

        when(postVentaService.findAll()).thenReturn(esperado);

        List<PostVentaResponseDTO> resultado =
                controller.findAll();

        assertEquals(1, resultado.size());
        assertEquals("Pokemon", resultado.getFirst().getNombreProducto());

        verify(postVentaService).findAll();
    }

    @Test
    void findBySeller_debeDevolverLista() {

        Long vendedorId = 10L;

        List<PostVentaResponseDTO> esperado =
                List.of(crearResponse());

        when(postVentaService.findBySeller(vendedorId))
                .thenReturn(esperado);

        List<PostVentaResponseDTO> resultado =
                controller.findBySeller(vendedorId);

        assertEquals(1, resultado.size());

        verify(postVentaService).findBySeller(vendedorId);
    }

    @Test
    void findByProduct_debeDevolverLista() {

        Long productoId = 100L;

        List<PostVentaResponseDTO> esperado =
                List.of(crearResponse());

        when(postVentaService.findByProduct(productoId))
                .thenReturn(esperado);

        List<PostVentaResponseDTO> resultado =
                controller.findByProduct(productoId);

        assertEquals(1, resultado.size());

        verify(postVentaService).findByProduct(productoId);
    }

    @Test
    void findActive_debeBuscarActivos() {

        List<PostVentaResponseDTO> esperado =
                List.of(crearResponse());

        when(postVentaService.findByEstado(EstadoPost.ACTIVO))
                .thenReturn(esperado);

        List<PostVentaResponseDTO> resultado =
                controller.findActive();

        assertEquals(1, resultado.size());

        verify(postVentaService)
                .findByEstado(EstadoPost.ACTIVO);
    }

    @Test
    void update_debeActualizarPostVenta() {

        Long id = 1L;

        PostVentaRequestDTO dto = new PostVentaRequestDTO();

        PostVentaResponseDTO esperado = crearResponse();

        when(postVentaService.update(id, dto))
                .thenReturn(esperado);

        PostVentaResponseDTO resultado =
                controller.update(id, dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(postVentaService).update(id, dto);
    }

    @Test
    void delete_debeEliminarPostVenta() {

        Long id = 1L;

        controller.delete(id);

        verify(postVentaService).delete(id);
    }

    @Test
    void convertirVentaAIntercambio_debeLlamarAlServicio() {

        Long idVenta = 1L;

        PostIntercambioRequestDTO dto =
                new PostIntercambioRequestDTO();

        controller.convertirVentaAIntercambio(idVenta, dto);

        verify(postVentaService)
                .convertirVentaAIntercambio(idVenta, dto);
    }
}
