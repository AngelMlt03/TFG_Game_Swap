package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.controller.HistorialController;
import com.tfg.angel.gameswap.backend.business.dto.response.CompraVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.IntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.impl.HistorialService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistorialControllerTest {

    @Mock
    private HistorialService historialService;

    @InjectMocks
    private HistorialController controller;

    @Test
    void historialCompras() {

        CompraVentaResponseDTO compra =
                CompraVentaResponseDTO.builder()
                        .id(1L)
                        .build();

        when(historialService.getHistorialCompras())
                .thenReturn(List.of(compra));

        List<CompraVentaResponseDTO> resultado =
                controller.historialCompras();

        assertEquals(1, resultado.size());
        verify(historialService).getHistorialCompras();
    }

    @Test
    void historialVentas() {

        CompraVentaResponseDTO venta =
                CompraVentaResponseDTO.builder()
                        .id(1L)
                        .build();

        when(historialService.getHistorialVentas())
                .thenReturn(List.of(venta));

        List<CompraVentaResponseDTO> resultado =
                controller.historialVentas();

        assertEquals(1, resultado.size());
        verify(historialService).getHistorialVentas();
    }

    @Test
    void historialIntercambios() {

        IntercambioResponseDTO intercambio =
                IntercambioResponseDTO.builder()
                        .id(1L)
                        .build();

        when(historialService.getHistorialIntercambios())
                .thenReturn(List.of(intercambio));

        List<IntercambioResponseDTO> resultado =
                controller.historialIntercambios();

        assertEquals(1, resultado.size());
        verify(historialService).getHistorialIntercambios();
    }
}
