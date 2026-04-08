package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.model.*;
import com.tfg.angel.gameswap.backend.business.repository.*;
import com.tfg.angel.gameswap.backend.business.service.impl.IntercambioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IntercambioServiceTest {

    @InjectMocks
    private IntercambioServiceImpl service;

    @Mock
    private IntercambioRepository intercambioRepository;
    @Mock
    private PostIntercambioRepository postIntercambioRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    void create_ok() {

        Usuario u1 = Usuario.builder().id(1L).build();
        Usuario u2 = Usuario.builder().id(2L).build();

        Producto producto = Producto.builder().id(1L).nombre("Juego 1").build();
        Producto productoCambio = Producto.builder().id(1L).nombre("Juego 2").build();

        PostIntercambio post = PostIntercambio.builder()
                .id(1L)
                .usuario(u1)
                .producto(producto)
                .productoCambio(productoCambio)
                .build();

        when(postIntercambioRepository.findById(1L)).thenReturn(Optional.of(post));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(u2));
        when(intercambioRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        var result = service.create(1L, 2L);

        assertNotNull(result);
    }

    @Test
    void create_same_user_error() {

        Usuario u1 = Usuario.builder().id(1L).build();

        PostIntercambio post = PostIntercambio.builder()
                .id(1L)
                .usuario(u1)
                .build();

        when(postIntercambioRepository.findById(1L)).thenReturn(Optional.of(post));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u1));

        assertThrows(RuntimeException.class, () -> service.create(1L, 1L));
    }
}