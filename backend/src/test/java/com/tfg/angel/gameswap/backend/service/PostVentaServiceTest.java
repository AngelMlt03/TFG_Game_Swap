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
import com.tfg.angel.gameswap.backend.business.repository.PostIntercambioRepository;
import com.tfg.angel.gameswap.backend.business.repository.PostVentaRepository;
import com.tfg.angel.gameswap.backend.business.repository.ProductoRepository;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.impl.PostVentaServiceImpl;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
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
    @Mock
    private PostIntercambioRepository postIntercambioRepository;
    @Mock
    private UsuarioDetailsService usuarioDetailsService;

    @InjectMocks
    private PostVentaServiceImpl postVentaService;

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

    private Usuario crearUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Angel");
        usuario.setNombreUsuario("angel");
        return usuario;
    }

    private Producto crearProducto() {
        Producto productoc = new Producto();
        productoc.setId(1L);
        productoc.setIdAPI(100);
        productoc.setNombre("FIFA");
        productoc.setEstado(EstadoProducto.NUEVO);
        return productoc;
    }

    private PostVenta crearPostVenta() {

        PostVenta post = new PostVenta();

        post.setId(1L);
        post.setVendedor(crearUsuario());
        post.setProducto(crearProducto());
        post.setPrecio(20.0);
        post.setPlataforma("PS5");
        post.setEstado(EstadoPost.ACTIVO);
        post.setDescripcion("Descripcion");

        return post;
    }

    private PostVentaRequestDTO crearDtoVenta() {

        PostVentaRequestDTO dto = new PostVentaRequestDTO();

        dto.setNombreProducto("FIFA");
        dto.setIdApi(100L);
        dto.setEstadoProducto("NUEVO");
        dto.setPlataforma("PS5");
        dto.setPrecio(20.0);
        dto.setDescripcion("Descripcion");

        return dto;
    }

    private PostIntercambioRequestDTO crearDtoIntercambio() {

        PostIntercambioRequestDTO dto = new PostIntercambioRequestDTO();

        dto.setNombreProducto("COD");
        dto.setIdApi(200L);
        dto.setEstadoProducto("USADO");
        dto.setDescripcion("Cambio");

        return dto;
    }

    @Test
    void create() {

        Usuario usuario = crearUsuario();

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        when(productoRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        when(postVentaRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        PostVentaResponseDTO dto =
                postVentaService.create(crearDtoVenta());

        assertNotNull(dto);

        verify(productoRepository).save(any(Producto.class));
        verify(postVentaRepository).save(any(PostVenta.class));
    }

    @Test
    void findById() {

        when(postVentaRepository.findById(1L))
                .thenReturn(Optional.of(crearPostVenta()));

        PostVentaResponseDTO dto =
                postVentaService.findById(1L);

        assertNotNull(dto);
    }

    @Test
    void findAll() {

        when(postVentaRepository.findAll())
                .thenReturn(List.of(crearPostVenta()));

        assertEquals(
                1,
                postVentaService.findAll().size()
        );
    }

    @Test
    void findBySeller() {

        when(postVentaRepository.findByVendedorId(1L))
                .thenReturn(List.of(crearPostVenta()));

        assertEquals(
                1,
                postVentaService.findBySeller(1L).size()
        );
    }

    @Test
    void findByProduct() {

        when(postVentaRepository.findByProductoId(1L))
                .thenReturn(List.of(crearPostVenta()));

        assertEquals(
                1,
                postVentaService.findByProduct(1L).size()
        );
    }

    @Test
    void findByEstado() {

        when(postVentaRepository.findByEstado(EstadoPost.ACTIVO))
                .thenReturn(List.of(crearPostVenta()));

        assertEquals(
                1,
                postVentaService.findByEstado(EstadoPost.ACTIVO).size()
        );
    }

    @Test
    void update() {

        PostVenta post = crearPostVenta();

        when(postVentaRepository.findById(1L))
                .thenReturn(Optional.of(post));

        PostVentaResponseDTO dto =
                postVentaService.update(1L, crearDtoVenta());

        assertNotNull(dto);

        verify(productoRepository).save(any(Producto.class));
        verify(postVentaRepository).save(post);
    }

    @Test
    void convertirVentaAIntercambio() {

        PostVenta venta = crearPostVenta();

        when(postVentaRepository.findById(1L))
                .thenReturn(Optional.of(venta));

        PostIntercambioRequestDTO dto =
                crearDtoIntercambio();

        postVentaService.convertirVentaAIntercambio(1L, dto);

        verify(productoRepository)
                .save(any(Producto.class));

        verify(postIntercambioRepository)
                .save(any(PostIntercambio.class));

        verify(postVentaRepository)
                .delete(venta);
    }

}
