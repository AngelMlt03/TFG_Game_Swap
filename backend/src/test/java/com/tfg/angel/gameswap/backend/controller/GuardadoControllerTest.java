package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.controller.GuardadoController;
import com.tfg.angel.gameswap.backend.business.dto.request.GuardadoRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostIntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.GuardadoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuardadoControllerTest {

    @Mock
    private GuardadoService guardadoService;

    @InjectMocks
    private GuardadoController controller;

    @Test
    void guardar() {

        GuardadoRequestDTO dto = new GuardadoRequestDTO();
        dto.setIdPost(1L);
        dto.setTipoPost("VENTA");

        controller.guardar(dto);

        verify(guardadoService).guardar(dto);
    }

    @Test
    void eliminar() {

        controller.eliminar(1L, "VENTA");

        verify(guardadoService).eliminar(1L, "VENTA");
    }

    @Test
    void getVentasGuardadas() {

        PostVentaResponseDTO venta =
                PostVentaResponseDTO.builder()
                        .id(1L)
                        .nombreProducto("FIFA")
                        .build();

        when(guardadoService.getVentasGuardadas())
                .thenReturn(List.of(venta));

        List<PostVentaResponseDTO> resultado =
                controller.getVentasGuardadas();

        assertEquals(1, resultado.size());
        assertEquals("FIFA", resultado.getFirst().getNombreProducto());
    }

    @Test
    void getIntercambiosGuardados() {

        PostIntercambioResponseDTO intercambio =
                PostIntercambioResponseDTO.builder()
                        .id(1L)
                        .nombreProducto("FIFA")
                        .build();

        when(guardadoService.getIntercambiosGuardados())
                .thenReturn(List.of(intercambio));

        List<PostIntercambioResponseDTO> resultado =
                controller.getIntercambiosGuardados();

        assertEquals(1, resultado.size());
        assertEquals("FIFA", resultado.getFirst().getNombreProducto());
    }

    @Test
    void existe() {

        when(guardadoService.existe(1L, "VENTA"))
                .thenReturn(true);

        boolean resultado = controller.existe(1L, "VENTA");

        assertTrue(resultado);
    }
}
