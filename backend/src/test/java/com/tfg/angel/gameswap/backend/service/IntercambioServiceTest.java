package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.response.IntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.*;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import com.tfg.angel.gameswap.backend.business.model.enums.Rol;
import com.tfg.angel.gameswap.backend.business.repository.*;
import com.tfg.angel.gameswap.backend.business.service.impl.IntercambioServiceImpl;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
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
    private Intercambio intercambio;
    private Usuario usuarioIntercambio;

    @BeforeEach
    void setUp() {

        Usuario usuarioProducto = Usuario.builder().id(1L).nombre("Angel").rol(Rol.CLIENTE).build();
        usuarioIntercambio = Usuario.builder().id(2L).nombre("Juan").rol(Rol.CLIENTE).build();

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

        intercambio = Intercambio.builder()
                .id(1L)
                .postIntercambio(post)
                .usuarioCambio(usuarioProducto)
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
        verify(intercambioRepository).save(any(Intercambio.class));
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

    @Test
    @DisplayName("Debe retornar una lista de DTOs si existen intercambios")
    void findAll_Success() {

        when(intercambioRepository.findAll()).thenReturn(List.of(intercambio));

        List<IntercambioResponseDTO> result = intercambioService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(intercambioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar una lista vacía si no hay intercambios")
    void findAll_Empty() {

        when(intercambioRepository.findAll()).thenReturn(Collections.emptyList());

        List<IntercambioResponseDTO> result = intercambioService.findAll();

        assertTrue(result.isEmpty());
        verify(intercambioRepository).findAll();
    }

    @Test
    @DisplayName("Debe retornar los intercambios asociados al ID del usuario")
    void findByUsuario_Success() {

        Long idUsuario = 1L;
        when(intercambioRepository.findByPostIntercambioUsuarioId(idUsuario)).thenReturn(List.of(intercambio));

        List<IntercambioResponseDTO> result = intercambioService.findByUsuario(idUsuario);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(intercambioRepository).findByPostIntercambioUsuarioId(idUsuario);
    }

    private PostIntercambio crearPost() {
        return PostIntercambio.builder()
                .id(1L)
                .build();
    }

    @Test
    void create_postNoEncontrado() {

        when(postIntercambioRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                GSNotFoundException.class,
                () -> intercambioService.create(1L, 1L)
        );
    }

    @Test
    void create_usuarioNoEncontrado() {

        PostIntercambio postne = crearPost();

        when(postIntercambioRepository.findById(1L))
                .thenReturn(Optional.of(postne));

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                GSNotFoundException.class,
                () -> intercambioService.create(1L, 1L)
        );
    }

    @Test
    void findById_notFound() {

        when(intercambioRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                GSNotFoundException.class,
                () -> intercambioService.findById(1L)
        );
    }

    @Test
    void delete_ok() {

        when(intercambioRepository.existsById(1L))
                .thenReturn(true);

        intercambioService.delete(1L);

        verify(intercambioRepository)
                .deleteById(1L);
    }

    @Test
    void delete_notFound() {

        when(intercambioRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(
                GSNotFoundException.class,
                () -> intercambioService.delete(1L)
        );
    }
}