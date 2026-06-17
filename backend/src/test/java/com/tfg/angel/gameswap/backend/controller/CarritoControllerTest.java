package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.controller.CarritoController;
import com.tfg.angel.gameswap.backend.business.dto.response.ProductoCarritoResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.CarritoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoControllerTest {

    @Mock
    private CarritoService service;

    @InjectMocks
    private CarritoController controller;

    @Test
    void create_debeLlamarAlServicio() {

        controller.create();

        verify(service).create();
    }

    @Test
    void findByUser_debeLlamarAlServicio() {

        Long userId = 1L;

        controller.findByUser(userId);

        verify(service).findByUser(userId);
    }

    @Test
    void getCarrito_debeDevolverListaProductos() {

        ProductoCarritoResponseDTO producto =
                ProductoCarritoResponseDTO.builder()
                        .id(1L)
                        .idPostVenta(2L)
                        .nombreProducto("Pokemon")
                        .plataforma("Switch")
                        .estado("NUEVO")
                        .precio(50.0)
                        .idApi(100L)
                        .build();

        List<ProductoCarritoResponseDTO> esperado = List.of(producto);

        when(service.getCarrito()).thenReturn(esperado);

        List<ProductoCarritoResponseDTO> resultado = controller.getCarrito();

        assertEquals(1, resultado.size());
        assertEquals("Pokemon", resultado.getFirst().getNombreProducto());

        verify(service).getCarrito();
    }

    @Test
    void agregarProducto_debeLlamarAlServicio() {

        Long postId = 10L;

        controller.agregarProducto(postId);

        verify(service).agregarProducto(postId);
    }

    @Test
    void eliminarProducto_debeLlamarAlServicio() {

        Long postId = 10L;

        controller.eliminarProducto(postId);

        verify(service).eliminarProducto(postId);
    }

    @Test
    void existe_debeDevolverTrue() {

        Long ventaId = 5L;

        when(service.estaEnCarrito(ventaId)).thenReturn(true);

        boolean resultado = controller.existe(ventaId);

        assertTrue(resultado);

        verify(service).estaEnCarrito(ventaId);
    }

    @Test
    void existe_debeDevolverFalse() {

        Long ventaId = 5L;

        when(service.estaEnCarrito(ventaId)).thenReturn(false);

        boolean resultado = controller.existe(ventaId);

        assertFalse(resultado);

        verify(service).estaEnCarrito(ventaId);
    }

    @Test
    void precioCarrito_debeDevolverPrecio() {

        when(service.getPrecioCarrito()).thenReturn(125.50);

        Double resultado = controller.precioCarrito();

        assertEquals(125.50, resultado);

        verify(service).getPrecioCarrito();
    }

    @Test
    void vaciar_debeLlamarAlServicio() {

        controller.vaciar();

        verify(service).vaciar();
    }
}