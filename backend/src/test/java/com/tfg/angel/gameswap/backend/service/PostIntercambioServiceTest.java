package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.request.PostIntercambioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostIntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.PostIntercambio;
import com.tfg.angel.gameswap.backend.business.model.Producto;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import com.tfg.angel.gameswap.backend.business.model.enums.Rol;
import com.tfg.angel.gameswap.backend.business.repository.PostIntercambioRepository;
import com.tfg.angel.gameswap.backend.business.repository.ProductoRepository;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.impl.PostIntercambioServiceImpl;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostIntercambioServiceTest {

    @Mock
    private PostIntercambioRepository postIntercambioRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private PostIntercambioServiceImpl postIntercambioService;

    private Usuario usuario;
    private Producto producto;
    private Producto productoCambio;
    private PostIntercambioRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder().id(1L).nombre("Angel").rol(Rol.CLIENTE).build();

        producto = Producto.builder()
                .id(1L)
                .nombre("Zelda: Breath of the Wild")
                .estado(EstadoProducto.NUEVO)
                .build();

        productoCambio = Producto.builder()
                .id(2L)
                .nombre("Elden Ring")
                .estado(EstadoProducto.NUEVO)
                .build();

        requestDTO = PostIntercambioRequestDTO.builder()
                .idUsuario(1L)
                .idProducto(1L)
                .idProductoCambio(2L)
                .build();
    }

    @Test
    @DisplayName("Debe crear un post de intercambio correctamente")
    void create_Success() {

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.findById(2L)).thenReturn(Optional.of(productoCambio));

        PostIntercambio postGuardado = PostIntercambio.builder()
                .id(50L)
                .usuario(usuario)
                .producto(producto)
                .productoCambio(productoCambio)
                .build();

        when(postIntercambioRepository.save(any(PostIntercambio.class))).thenReturn(postGuardado);

        PostIntercambioResponseDTO result = postIntercambioService.create(requestDTO);

        assertNotNull(result);
        verify(postIntercambioRepository).save(any(PostIntercambio.class));
        verify(usuarioRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar GSNotFoundException si el producto deseado no existe al crear")
    void create_ThrowsNotFound_WhenProductoCambioMissing() {

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.findById(2L)).thenReturn(Optional.empty()); // No existe el deseado

        assertThrows(GSNotFoundException.class, () -> postIntercambioService.create(requestDTO));
        verify(postIntercambioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe buscar un post por ID correctamente")
    void findById_Success() {

        PostIntercambio post = PostIntercambio.builder()
                .id(50L)
                .usuario(usuario)
                .producto(producto)
                .productoCambio(productoCambio)
                .build();

        when(postIntercambioRepository.findById(50L)).thenReturn(Optional.of(post));

        PostIntercambioResponseDTO result = postIntercambioService.findById(50L);

        assertNotNull(result);
        assertEquals(50L, post.getId());
    }

    @Test
    @DisplayName("Debe actualizar un post existente correctamente")
    void update_Success() {

        PostIntercambio postExistente = PostIntercambio.builder().id(50L).usuario(usuario).build();

        when(postIntercambioRepository.findById(50L)).thenReturn(Optional.of(postExistente));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.findById(2L)).thenReturn(Optional.of(productoCambio));
        when(postIntercambioRepository.save(any(PostIntercambio.class))).thenReturn(postExistente);

        PostIntercambioResponseDTO result = postIntercambioService.update(50L, requestDTO);

        assertNotNull(result);
        verify(postIntercambioRepository).save(postExistente);
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar actualizar un post que no existe")
    void update_ThrowsNotFound() {

        when(postIntercambioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(GSNotFoundException.class, () -> postIntercambioService.update(99L, requestDTO));
        verify(postIntercambioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe eliminar un post si existe")
    void delete_Success() {

        when(postIntercambioRepository.existsById(50L)).thenReturn(true);

        postIntercambioService.delete(50L);

        verify(postIntercambioRepository).deleteById(50L);
    }

    @Test
    @DisplayName("Debe lanzar GSNotFoundException al intentar eliminar un post inexistente")
    void delete_ThrowsNotFound() {

        when(postIntercambioRepository.existsById(50L)).thenReturn(false);

        assertThrows(GSNotFoundException.class, () -> postIntercambioService.delete(50L));
        verify(postIntercambioRepository, never()).deleteById(anyLong());
    }
}
