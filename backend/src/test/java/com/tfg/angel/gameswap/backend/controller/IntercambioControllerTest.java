package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.controller.IntercambioController;
import com.tfg.angel.gameswap.backend.business.dto.response.IntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.IntercambioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IntercambioControllerTest {

    @Mock
    private IntercambioService service;

    @InjectMocks
    private IntercambioController controller;

    private IntercambioResponseDTO crearIntercambio() {
        return IntercambioResponseDTO.builder()
                .id(1L)
                .idPostIntercambio(2L)

                .idUsuarioPublicador(10L)
                .nombreUsuarioPublicador("usuarioPublicador")

                .idUsuarioCambio(20L)
                .nombreUsuarioCambio("usuarioCambio")

                .idProductoOfrecido(100L)
                .idApiProductoOfrecido(1000L)
                .nombreProductoOfrecido("Pokemon")
                .plataformaProductoOfrecido("Switch")
                .estadoProductoOfrecido("NUEVO")

                .idProductoDeseado(200L)
                .idApiProductoDeseado(2000L)
                .nombreProductoDeseado("Zelda")
                .plataformaProductoDeseado("Switch")
                .estadoProductoDeseado("USADO")

                .descripcion("Intercambio de prueba")
                .fecha(LocalDate.now())
                .build();
    }

    @Test
    void create_debeCrearIntercambio() {

        Long postId = 5L;

        IntercambioResponseDTO esperado = crearIntercambio();

        when(service.create(postId, 1L)).thenReturn(esperado);

        IntercambioResponseDTO resultado = controller.create(postId);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Pokemon", resultado.getNombreProductoOfrecido());

        verify(service).create(postId, 1L);
    }

    @Test
    void findById_debeDevolverIntercambio() {

        Long id = 1L;

        IntercambioResponseDTO esperado = crearIntercambio();

        when(service.findById(id)).thenReturn(esperado);

        IntercambioResponseDTO resultado = controller.findById(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());

        verify(service).findById(id);
    }

    @Test
    void findAll_debeDevolverLista() {

        List<IntercambioResponseDTO> esperado =
                List.of(crearIntercambio());

        when(service.findAll()).thenReturn(esperado);

        List<IntercambioResponseDTO> resultado = controller.findAll();

        assertEquals(1, resultado.size());
        assertEquals(
                "Pokemon",
                resultado.getFirst().getNombreProductoOfrecido()
        );

        verify(service).findAll();
    }

    @Test
    void findByUsuario_debeDevolverLista() {

        Long usuarioId = 10L;

        List<IntercambioResponseDTO> esperado =
                List.of(crearIntercambio());

        when(service.findByUsuario(usuarioId)).thenReturn(esperado);

        List<IntercambioResponseDTO> resultado =
                controller.findByUsuario(usuarioId);

        assertEquals(1, resultado.size());

        verify(service).findByUsuario(usuarioId);
    }

    @Test
    void delete_debeLlamarAlServicio() {

        Long id = 1L;

        controller.delete(id);

        verify(service).delete(id);
    }
}
