package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.controller.CarritoController;
import com.tfg.angel.gameswap.backend.business.dto.request.CarritoRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.request.ProductoCarritoRequestDTO;
import com.tfg.angel.gameswap.backend.business.service.CarritoService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CarritoControllerTest {

    @Test
    void testAllMethods() {

        CarritoService service = mock(CarritoService.class);
        CarritoController controller = new CarritoController(service);
        CarritoRequestDTO carritoRequestDTO = new CarritoRequestDTO(1L);

        controller.findByUser(1L);
        //controller.addProduct(1L);
        //.removeProduct(new ProductoCarritoRequestDTO(1L,1L));
        //controller.create(carritoRequestDTO);
        //controller.delete(1L);

        verify(service).findByUser(1L);
        //verify(service).addProduct(1L);
        //verify(service).removeProduct(1L);
        //verify(service).create(carritoRequestDTO);
        verify(service).delete(1L);
    }
}
