package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.controller.ProductoController;
import com.tfg.angel.gameswap.backend.business.dto.request.ProductoRequestDTO;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import com.tfg.angel.gameswap.backend.business.service.ProductoService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ProductoControllerTest {

    @Test
    void testAllMethods() {

        ProductoService service = mock(ProductoService.class);
        ProductoController controller = new ProductoController(service);

        ProductoRequestDTO productoRequestDTO = new ProductoRequestDTO();

        controller.findAll();
        controller.findById(1L);
        controller.findByName("Juego");
        controller.findByState(EstadoProducto.NUEVO.toString());
        controller.create(productoRequestDTO);
        controller.update(1L, productoRequestDTO);
        controller.delete(1L);

        verify(service).findAll();
        verify(service).findById(1L);
        verify(service).findByName("Juego");
        verify(service).findByState(EstadoProducto.NUEVO.toString());
        verify(service).create(productoRequestDTO);
        verify(service).update(1L, productoRequestDTO);
        verify(service).delete(1L);
    }
}
