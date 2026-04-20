package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.controller.CompraVentaController;
import com.tfg.angel.gameswap.backend.business.service.CompraVentaService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CompraVentaControllerTest {

    @Test
    void testAllMethods() {

        CompraVentaService service = mock(CompraVentaService.class);
        CompraVentaController controller = new CompraVentaController(service);

        controller.findAll();
        controller.findById(1L);
        controller.findByComprador(1L);
        controller.findByVendedor(1L);
        controller.create(1L);
        controller.delete(1L);

        verify(service).findAll();
        verify(service).findById(1L);
        verify(service).findByComprador(1L);
        verify(service).findByVendedor(1L);
        verify(service).create(1L,1L);
        verify(service).delete(1L);
    }
}
