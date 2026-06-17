package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.controller.PostIntercambioController;
import com.tfg.angel.gameswap.backend.business.dto.request.PostIntercambioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.request.PostVentaRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostIntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.PostIntercambioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostIntercambioControllerTest {

    @Mock
    private PostIntercambioService postIntercambioService;

    @InjectMocks
    private PostIntercambioController controller;

    private PostIntercambioResponseDTO crearResponse() {
        return PostIntercambioResponseDTO.builder()
                .id(1L)
                .idUsuario(10L)
                .nombreUsuario("angel")

                .idProducto(100L)
                .idApi(1000L)
                .nombreProducto("Pokemon")
                .estado("NUEVO")
                .plataforma("Switch")

                .idProductoIntercambio(200L)
                .idApiProductoIntercambio(2000L)
                .nombreProductoIntercambio("Zelda")
                .estadoIntercambio("USADO")
                .plataformaIntercambio("Switch")

                .descripcion("Intercambio de prueba")
                .build();
    }

    @Test
    void create_debeCrearPostIntercambio() {

        PostIntercambioRequestDTO dto = new PostIntercambioRequestDTO();

        PostIntercambioResponseDTO esperado = crearResponse();

        when(postIntercambioService.create(dto)).thenReturn(esperado);

        PostIntercambioResponseDTO resultado = controller.create(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(postIntercambioService).create(dto);
    }

    @Test
    void findById_debeDevolverPost() {

        Long id = 1L;

        PostIntercambioResponseDTO esperado = crearResponse();

        when(postIntercambioService.findById(id)).thenReturn(esperado);

        PostIntercambioResponseDTO resultado = controller.findById(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());

        verify(postIntercambioService).findById(id);
    }

    @Test
    void findAll_debeDevolverLista() {

        List<PostIntercambioResponseDTO> esperado =
                List.of(crearResponse());

        when(postIntercambioService.findAll()).thenReturn(esperado);

        List<PostIntercambioResponseDTO> resultado =
                controller.findAll();

        assertEquals(1, resultado.size());

        verify(postIntercambioService).findAll();
    }

    @Test
    void findByUser_debeDevolverLista() {

        Long usuarioId = 10L;

        List<PostIntercambioResponseDTO> esperado =
                List.of(crearResponse());

        when(postIntercambioService.findByUser(usuarioId))
                .thenReturn(esperado);

        List<PostIntercambioResponseDTO> resultado =
                controller.findByUser(usuarioId);

        assertEquals(1, resultado.size());

        verify(postIntercambioService).findByUser(usuarioId);
    }

    @Test
    void update_debeActualizarPost() {

        Long id = 1L;

        PostIntercambioRequestDTO dto =
                new PostIntercambioRequestDTO();

        PostIntercambioResponseDTO esperado =
                crearResponse();

        when(postIntercambioService.update(id, dto))
                .thenReturn(esperado);

        PostIntercambioResponseDTO resultado =
                controller.update(id, dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(postIntercambioService).update(id, dto);
    }

    @Test
    void delete_debeEliminarPost() {

        Long id = 1L;

        controller.delete(id);

        verify(postIntercambioService).delete(id);
    }

    @Test
    void convertirIntercambioAVenta_debeLlamarAlServicio() {

        Long id = 1L;

        PostVentaRequestDTO dto =
                new PostVentaRequestDTO();

        controller.convertirIntercambioAVenta(id, dto);

        verify(postIntercambioService)
                .convertirIntercambioAVenta(id, dto);
    }

    @Test
    void existeIntercambioSugerido_true() {

        when(postIntercambioService.existeIntercambioInverso(
                "Pokemon",
                "Zelda"))
                .thenReturn(true);

        boolean resultado =
                controller.existeIntercambioSugerido(
                        "Pokemon",
                        "Zelda"
                );

        assertTrue(resultado);

        verify(postIntercambioService)
                .existeIntercambioInverso(
                        "Pokemon",
                        "Zelda"
                );
    }

    @Test
    void existeIntercambioSugerido_false() {

        when(postIntercambioService.existeIntercambioInverso(
                "Pokemon",
                "Zelda"))
                .thenReturn(false);

        boolean resultado =
                controller.existeIntercambioSugerido(
                        "Pokemon",
                        "Zelda"
                );

        assertFalse(resultado);

        verify(postIntercambioService)
                .existeIntercambioInverso(
                        "Pokemon",
                        "Zelda"
                );
    }
}
