package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.controller.HomeController;
import com.tfg.angel.gameswap.backend.business.dto.response.HomeStatsDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostBusquedaDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.UsuarioRankingDTO;
import com.tfg.angel.gameswap.backend.business.service.HomeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @Mock
    private HomeService homeService;

    @InjectMocks
    private HomeController controller;

    @Test
    void ultimasVentas() {

        PostBusquedaDTO post = PostBusquedaDTO.builder()
                .id(1L)
                .tipo("VENTA")
                .build();

        when(homeService.ultimasVentas())
                .thenReturn(List.of(post));

        List<PostBusquedaDTO> resultado =
                controller.ultimasVentas();

        assertEquals(1, resultado.size());
        verify(homeService).ultimasVentas();
    }

    @Test
    void ultimosIntercambios() {

        PostBusquedaDTO post = PostBusquedaDTO.builder()
                .id(1L)
                .tipo("INTERCAMBIO")
                .build();

        when(homeService.ultimosIntercambios())
                .thenReturn(List.of(post));

        List<PostBusquedaDTO> resultado =
                controller.ultimosIntercambios();

        assertEquals(1, resultado.size());
        verify(homeService).ultimosIntercambios();
    }

    @Test
    void estadisticas() {

        HomeStatsDTO stats = HomeStatsDTO.builder()
                .ventas(10L)
                .intercambios(5L)
                .usuarios(20L)
                .reviews(50L)
                .build();

        when(homeService.estadisticas())
                .thenReturn(stats);

        HomeStatsDTO resultado =
                controller.estadisticas();

        assertEquals(10L, resultado.getVentas());
        verify(homeService).estadisticas();
    }

    @Test
    void topUsuarios() {

        UsuarioRankingDTO usuario = UsuarioRankingDTO.builder()
                .nombreUsuario("angel")
                .estrellas(5.0)
                .build();

        when(homeService.topUsuarios())
                .thenReturn(List.of(usuario));

        List<UsuarioRankingDTO> resultado =
                controller.topUsuarios();

        assertEquals(1, resultado.size());
        assertEquals("angel", resultado.getFirst().getNombreUsuario());

        verify(homeService).topUsuarios();
    }
}
