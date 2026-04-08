package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.response.IntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.*;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import com.tfg.angel.gameswap.backend.business.repository.*;
import com.tfg.angel.gameswap.backend.business.service.impl.IntercambioServiceImpl;
import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IntercambioServiceTest {

    @Mock
    private IntercambioRepository intercambioRepository;
    @Mock
    private PostIntercambioRepository postIntercambioRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private IntercambioServiceImpl intercambioService;

    private PostIntercambio post;
    private Usuario usuarioProducto;
    private Usuario usuarioIntercambio;

    @BeforeEach
    void setUp() {

        usuarioProducto = Usuario.builder().id(1L).nombre("Angel").build();
        usuarioIntercambio = Usuario.builder().id(2L).nombre("Juan").build();

        Producto producto = Producto.builder()
                .id(1L)
                .idAPI(1)
                .nombre("Juego")
                .estado(EstadoProducto.NUEVO)
                .build();

        post = PostIntercambio.builder()
                .id(10L)
                .usuario(usuarioProducto)
                .producto(producto)
                .productoCambio(producto)
                .estado(EstadoPost.ACTIVO)
                .build();
    }

    @Test
    @DisplayName("Debe crear un intercambio correctamente cuando los datos son válidos")
    void create_Success() {

        when(postIntercambioRepository.findById(10L)).thenReturn(Optional.of(post));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuarioIntercambio));

        Intercambio intercambioGuardado = Intercambio.builder()
                .id(100L)
                .postIntercambio(post)
                .usuarioCambio(usuarioIntercambio)
                .build();
        when(intercambioRepository.save(any(Intercambio.class))).thenReturn(intercambioGuardado);

        IntercambioResponseDTO response = intercambioService.create(10L, 2L);

        assertNotNull(response);
        assertEquals(EstadoPost.FINALIZADO, post.getEstado());
        verify(postIntercambioRepository, times(2)).save(post);
        verify(intercambioRepository).save(any(Intercambio.class));
    }

    @Test
    @DisplayName("Debe lanzar GSBadRequestException si el usuario intenta intercambiar con su propio post")
    void create_ThrowsBadRequest_WhenSameUser() {

        when(postIntercambioRepository.findById(10L)).thenReturn(Optional.of(post));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioProducto));

        assertThrows(GSBadRequestException.class, () -> {
            intercambioService.create(10L, 1L);
        });

        verify(intercambioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar GSNotFoundException si el post no existe")
    void create_ThrowsNotFound_WhenPostMissing() {

        when(postIntercambioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(GSNotFoundException.class, () -> intercambioService.create(99L, 2L));
    }

    @Test
    @DisplayName("Debe retornar un DTO cuando el ID existe")
    void findById_Success() {

        Intercambio intercambio = Intercambio.builder().id(1L).postIntercambio(post).usuarioCambio(usuarioIntercambio).build();
        when(intercambioRepository.findById(1L)).thenReturn(Optional.of(intercambio));

        IntercambioResponseDTO result = intercambioService.findById(1L);

        assertNotNull(result);
        verify(intercambioRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar borrar un intercambio inexistente")
    void delete_ThrowsNotFound() {

        when(intercambioRepository.existsById(1L)).thenReturn(false);

        assertThrows(GSNotFoundException.class, () -> intercambioService.delete(1L));
        verify(intercambioRepository, never()).deleteById(anyLong());
    }
}