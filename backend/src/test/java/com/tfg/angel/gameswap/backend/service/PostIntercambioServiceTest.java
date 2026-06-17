package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.request.PostIntercambioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.request.PostVentaRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostIntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.PostIntercambio;
import com.tfg.angel.gameswap.backend.business.model.PostVenta;
import com.tfg.angel.gameswap.backend.business.model.Producto;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
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

import java.util.List;
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

    private PostIntercambioRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        Usuario.builder().id(1L).nombre("Angel").rol(Rol.CLIENTE).build();

        Producto.builder()
                .id(1L)
                .nombre("Zelda: Breath of the Wild")
                .estado(EstadoProducto.NUEVO)
                .build();

        Producto.builder()
                .id(2L)
                .nombre("Elden Ring")
                .estado(EstadoProducto.NUEVO)
                .build();

        requestDTO = PostIntercambioRequestDTO.builder()
                .build();
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

    private Producto productoCambio() {
        return Producto.builder()
                .id(2L)
                .nombre("Mario")
                .idAPI(200)
                .estado(EstadoProducto.USADO)
                .build();
    }

    private PostIntercambio post() {
        return PostIntercambio.builder()
                .id(1L)
                .usuario(usuario())
                .producto(producto())
                .productoCambio(productoCambio())
                .plataforma("Switch")
                .plataformaCambio("PS5")
                .descripcion("Descripcion")
                .estado(EstadoPost.ACTIVO)
                .build();
    }

    private PostIntercambioRequestDTO dto() {
        PostIntercambioRequestDTO dto = new PostIntercambioRequestDTO();

        dto.setNombreProducto("Zelda");
        dto.setIdApi(100L);
        dto.setEstadoProducto("NUEVO");

        dto.setNombreProductoIntercambio("Mario");
        dto.setIdApiIntercambio(200L);
        dto.setEstadoProductoIntercambio("USADO");

        dto.setPlataforma("Switch");
        dto.setPlataformaIntercambio("PS5");
        dto.setDescripcion("Descripcion");

        return dto;
    }

    @Test
    void findById_ok() {

        when(postIntercambioRepository.findById(1L))
                .thenReturn(Optional.of(post()));

        PostIntercambioResponseDTO resultado =
                postIntercambioService.findById(1L);

        assertNotNull(resultado);
    }

    @Test
    void findById_notFound() {

        when(postIntercambioRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                GSNotFoundException.class,
                () -> postIntercambioService.findById(1L)
        );
    }

    @Test
    void findAll_ok() {

        when(postIntercambioRepository.findAll())
                .thenReturn(List.of(post()));

        List<PostIntercambioResponseDTO> resultado =
                postIntercambioService.findAll();

        assertEquals(1, resultado.size());
    }

    @Test
    void findByUser_ok() {

        when(postIntercambioRepository.findByUsuarioId(1L))
                .thenReturn(List.of(post()));

        List<PostIntercambioResponseDTO> resultado =
                postIntercambioService.findByUser(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void findByProduct_ok() {

        when(postIntercambioRepository.findByProductoId(1L))
                .thenReturn(List.of(post()));

        List<PostIntercambioResponseDTO> resultado =
                postIntercambioService.findByProduct(1L);

        assertEquals(1, resultado.size());
    }

    @Test
    void findByEstado_ok() {

        when(postIntercambioRepository.findByEstado(EstadoPost.ACTIVO))
                .thenReturn(List.of(post()));

        List<PostIntercambioResponseDTO> resultado =
                postIntercambioService.findByEstado(EstadoPost.ACTIVO);

        assertEquals(1, resultado.size());
    }

    @Test
    void update_ok() {

        PostIntercambio existente = post();

        when(postIntercambioRepository.findById(1L))
                .thenReturn(Optional.of(existente));

        PostIntercambioResponseDTO resultado =
                postIntercambioService.update(1L, dto());

        assertNotNull(resultado);

        verify(productoRepository, times(2))
                .save(any(Producto.class));

        verify(postIntercambioRepository)
                .save(existente);
    }

    @Test
    void delete_ok() {

        when(postIntercambioRepository.existsById(1L))
                .thenReturn(true);

        postIntercambioService.delete(1L);

        verify(postIntercambioRepository)
                .deleteById(1L);
    }

    @Test
    void delete_notFound() {

        when(postIntercambioRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(
                GSNotFoundException.class,
                () -> postIntercambioService.delete(1L)
        );
    }

    @Test
    void existeIntercambioInverso_true() {

        when(postIntercambioRepository.existeIntercambioInverso(
                "Zelda",
                "Mario"))
                .thenReturn(true);

        assertTrue(
                postIntercambioService.existeIntercambioInverso(
                        "Zelda",
                        "Mario")
        );
    }

    @Test
    void existeIntercambioInverso_false() {

        when(postIntercambioRepository.existeIntercambioInverso(
                "Zelda",
                "Mario"))
                .thenReturn(false);

        assertFalse(
                postIntercambioService.existeIntercambioInverso(
                        "Zelda",
                        "Mario")
        );
    }
}
