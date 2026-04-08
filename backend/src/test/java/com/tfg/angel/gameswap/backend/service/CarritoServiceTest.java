package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.model.*;
import com.tfg.angel.gameswap.backend.business.repository.*;
import com.tfg.angel.gameswap.backend.business.service.impl.CarritoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @InjectMocks
    private CarritoServiceImpl service;

    @Mock
    private CarritoRepository carritoRepository;
    @Mock
    private ProductoCarritoRepository productoCarritoRepository;
    @Mock
    private PostVentaRepository postVentaRepository;

    @Test
    void addProduct_ok() {

        Usuario usuario = Usuario.builder()
                .id(1L)
                .build();

        Carrito carrito = Carrito.builder()
                .id(1L)
                .coste(0.0)
                .usuario(usuario)
                .productos(List.of())
                .build();

        PostVenta post = PostVenta.builder()
                .id(1L)
                .precio(20.0)
                .build();

        when(carritoRepository.findByUsuarioId(1L)).thenReturn(Optional.of(carrito));
        when(postVentaRepository.findById(1L)).thenReturn(Optional.of(post));

        service.addProduct(1L, 1L);

        verify(productoCarritoRepository).save(any());
    }
}
