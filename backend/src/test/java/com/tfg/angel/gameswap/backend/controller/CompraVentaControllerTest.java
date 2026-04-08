package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.service.CompraVentaService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CompraVentaControllerTest {

    @Test
    void controller_calls_service() {

        CompraVentaService service = mock(CompraVentaService.class);

        service.create(1L, 1L);

        verify(service).create(1L, 1L);
    }
}
