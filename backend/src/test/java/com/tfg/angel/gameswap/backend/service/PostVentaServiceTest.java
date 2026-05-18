package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.request.PostVentaRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.PostVenta;
import com.tfg.angel.gameswap.backend.business.model.Producto;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import com.tfg.angel.gameswap.backend.business.model.enums.Rol;
import com.tfg.angel.gameswap.backend.business.repository.PostVentaRepository;
import com.tfg.angel.gameswap.backend.business.repository.ProductoRepository;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.impl.PostVentaServiceImpl;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostVentaServiceTest {

    @Mock
    private PostVentaRepository postVentaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private PostVentaServiceImpl postVentaService;

    private Usuario vendedor;
    private Producto producto;
    private PostVentaRequestDTO requestDTO;
    private PostVenta postVenta;

    @BeforeEach
    void setUp() {

        vendedor = Usuario.builder().id(1L).nombre("Angel").rol(Rol.CLIENTE).build();

        producto = Producto.builder()
                .id(1L)
                .nombre("Elden Ring")
                .estado(EstadoProducto.NUEVO)
                .build();

        requestDTO = PostVentaRequestDTO.builder()
                .idApi(1L)
                .nombreProducto("Elden Ring")
                .precio(50.0)
                .build();

        postVenta = PostVenta.builder()
                .id(100L)
                .vendedor(vendedor)
                .producto(producto)
                .precio(50.0)
                .estado(EstadoPost.ACTIVO)
                .build();
    }

    @Test
    @DisplayName("Debe crear un post de venta correctamente")
    void create_Success() {

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(postVentaRepository.save(any(PostVenta.class))).thenReturn(postVenta);

        PostVentaResponseDTO response = postVentaService.create(requestDTO);

        assertNotNull(response);
        verify(postVentaRepository).save(any(PostVenta.class));
        verify(usuarioRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar GSNotFoundException si el vendedor no existe al crear")
    void create_ThrowsNotFound_WhenVendedorMissing() {

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GSNotFoundException.class, () -> postVentaService.create(requestDTO));
        verify(postVentaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe buscar un post de venta por ID")
    void findById_Success() {

        when(postVentaRepository.findById(100L)).thenReturn(Optional.of(postVenta));

        PostVentaResponseDTO response = postVentaService.findById(100L);

        assertNotNull(response);
        verify(postVentaRepository).findById(100L);
    }

    @Test
    @DisplayName("Debe actualizar un post de venta correctamente")
    void update_Success() {

        when(postVentaRepository.findById(100L)).thenReturn(Optional.of(postVenta));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(postVentaRepository.save(any(PostVenta.class))).thenReturn(postVenta);

        PostVentaResponseDTO response = postVentaService.update(100L, requestDTO);

        assertNotNull(response);
        verify(postVentaRepository).save(postVenta);
    }

    @Test
    @DisplayName("Debe lanzar GSNotFoundException al intentar borrar un post que no existe")
    void delete_ThrowsNotFound() {

        when(postVentaRepository.existsById(100L)).thenReturn(false);

        assertThrows(GSNotFoundException.class, () -> postVentaService.delete(100L));
        verify(postVentaRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Debe eliminar el post de venta si existe")
    void delete_Success() {

        when(postVentaRepository.existsById(100L)).thenReturn(true);

        postVentaService.delete(100L);

        verify(postVentaRepository).deleteById(100L);
    }

    @Test
    @DisplayName("Debe retornar la lista completa de posts")
    void findAll_Success() {

        when(postVentaRepository.findAll()).thenReturn(List.of(postVenta, postVenta));

        List<PostVentaResponseDTO> result = postVentaService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(postVentaRepository).findAll();
    }

    @Test
    @DisplayName("Debe retornar posts filtrados por ID de vendedor")
    void findBySeller_Success() {

        when(postVentaRepository.findByVendedorId(1L)).thenReturn(List.of(postVenta));

        List<PostVentaResponseDTO> result = postVentaService.findBySeller(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(postVentaRepository).findByVendedorId(1L);
    }

    @Test
    @DisplayName("Debe retornar posts filtrados por ID de producto")
    void findByProduct_Success() {

        when(postVentaRepository.findByProductoId(1L)).thenReturn(List.of(postVenta));

        List<PostVentaResponseDTO> result = postVentaService.findByProduct(1L);

        assertFalse(result.isEmpty());
        verify(postVentaRepository).findByProductoId(1L);
    }

    @Test
    @DisplayName("Debe retornar posts filtrados por estado (Enum)")
    void findByEstado_Success() {

        when(postVentaRepository.findByEstado(EstadoPost.ACTIVO)).thenReturn(List.of(postVenta));

        List<PostVentaResponseDTO> result = postVentaService.findByEstado(EstadoPost.ACTIVO);

        assertFalse(result.isEmpty());
        verify(postVentaRepository).findByEstado(EstadoPost.ACTIVO);
    }

}
