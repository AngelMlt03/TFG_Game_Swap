package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.request.PostIntercambioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.request.PostVentaRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.PostIntercambio;
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
import java.util.NoSuchElementException;
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

    private Usuario usuario() {
        return Usuario.builder()
                .id(1L)
                .nombre("Angel")
                .build();
    }

    private Producto producto() {
        return Producto.builder()
                .id(1L)
                .nombre("Zelda")
                .idAPI(100)
                .estado(EstadoProducto.NUEVO)
                .build();
    }

    private PostVenta postVenta() {
        return PostVenta.builder()
                .id(1L)
                .vendedor(usuario())
                .producto(producto())
                .plataforma("Switch")
                .precio(50.0)
                .descripcion("Descripcion")
                .estado(EstadoPost.ACTIVO)
                .build();
    }

    private PostVentaRequestDTO ventaDTO() {
        PostVentaRequestDTO dto = new PostVentaRequestDTO();

        dto.setIdApi(100L);
        dto.setNombreProducto("Zelda");
        dto.setEstadoProducto("NUEVO");
        dto.setPlataforma("Switch");
        dto.setPrecio(50.0);
        dto.setDescripcion("Descripcion");

        return dto;
    }

    @Test
    void findById_notFound() {

        when(postVentaRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                GSNotFoundException.class,
                () -> postVentaService.findById(1L)
        );
    }

    @Test
    void findAll_ok() {

        when(postVentaRepository.findAll())
                .thenReturn(List.of(postVenta()));

        List<PostVentaResponseDTO> resultado =
                postVentaService.findAll();

        assertEquals(1, resultado.size());
    }

    @Test
    void findBySeller_ok() {

        when(postVentaRepository.findByVendedorId(1L))
                .thenReturn(List.of(postVenta()));

        List<PostVentaResponseDTO> resultado =
                postVentaService.findBySeller(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void findByProduct_ok() {

        when(postVentaRepository.findByProductoId(1L))
                .thenReturn(List.of(postVenta()));

        List<PostVentaResponseDTO> resultado =
                postVentaService.findByProduct(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void findByEstado_ok() {

        when(postVentaRepository.findByEstado(EstadoPost.ACTIVO))
                .thenReturn(List.of(postVenta()));

        List<PostVentaResponseDTO> resultado =
                postVentaService.findByEstado(EstadoPost.ACTIVO);

        assertEquals(1, resultado.size());
    }

    @Test
    void update_ok() {

        PostVenta existente = postVenta();

        when(postVentaRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        PostVentaResponseDTO resultado =
                postVentaService.update(1L, ventaDTO());

        assertNotNull(resultado);

        verify(productoRepository)
                .save(any(Producto.class));

        verify(postVentaRepository)
                .save(existente);
    }

    @Test
    void delete_ok() {

        when(postVentaRepository.existsById(1L))
                .thenReturn(true);

        postVentaService.delete(1L);

        verify(postVentaRepository)
                .deleteById(1L);
    }

    @Test
    void delete_notFound() {

        when(postVentaRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(
                GSNotFoundException.class,
                () -> postVentaService.delete(1L)
        );
    }

}
