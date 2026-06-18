package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.controller.TransaccionController;
import com.tfg.angel.gameswap.backend.business.service.impl.TransaccionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransaccionControllerTest {

    @Mock
    private TransaccionService transaccionService;

    @InjectMocks
    private TransaccionController controller;

    @Test
    void comprar() {

        when(transaccionService.comprar(1L))
                .thenReturn(50.0);

        Double resultado = controller.comprar(1L);

        assertEquals(50.0, resultado);
        verify(transaccionService).comprar(1L);
    }

    @Test
    void intercambiar() {

        controller.intercambiar(1L);

        verify(transaccionService).intercambiar(1L);
    }
}
