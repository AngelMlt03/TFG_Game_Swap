package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.controller.IntercambioController;
import com.tfg.angel.gameswap.backend.business.service.IntercambioService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class IntercambioControllerTest {

    @Test
    void testAllMethods() {

        IntercambioService service = mock(IntercambioService.class);
        IntercambioController controller = new IntercambioController(service);

        controller.findAll();
        controller.findById(1L);
        controller.findByUsuario(1L);
        controller.create(1L);
        controller.delete(1L);

        verify(service).findAll();
        verify(service).findById(1L);
        verify(service).findByUsuario(1L);
        verify(service).create(1L,1L);
        verify(service).delete(1L);
    }
}
