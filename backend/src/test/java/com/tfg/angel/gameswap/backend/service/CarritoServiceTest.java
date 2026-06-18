package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.response.ProductoCarritoResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.*;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import com.tfg.angel.gameswap.backend.business.repository.*;
import com.tfg.angel.gameswap.backend.business.service.impl.CarritoServiceImpl;
import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private PostVentaRepository postVentaRepository;

    @Mock
    private ProductoCarritoRepository productoCarritoRepository;

    @Mock
    private UsuarioDetailsService usuarioDetailsService;

    @Spy
    @InjectMocks
    private CarritoServiceImpl service;

    private Usuario usuario() {
        return Usuario.builder()
                .id(1L)
                .nombre("Angel")
                .build();
    }

    @Test
    void create_debeCrearCarrito() {

        Usuario usuario = usuario();

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        service.create();

        verify(carritoRepository).save(any(Carrito.class));
    }

    @Test
    void findByUser_notFound() {

        when(carritoRepository.findByUsuarioId(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                GSNotFoundException.class,
                () -> service.findByUser(1L)
        );
    }

    @Test
    void delete_ok() {

        when(carritoRepository.existsById(1L))
                .thenReturn(true);

        service.delete(1L);

        verify(carritoRepository).deleteById(1L);
    }

    @Test
    void delete_notFound() {

        when(carritoRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(
                GSNotFoundException.class,
                () -> service.delete(1L)
        );
    }

    @Test
    void getPrecioCarrito() {

        Usuario usuario = usuario();

        Carrito carrito = Carrito.builder()
                .coste(35.0)
                .build();

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        when(carritoRepository.findByUsuarioId(1L))
                .thenReturn(Optional.of(carrito));

        Double resultado = service.getPrecioCarrito();

        assertEquals(35.0, resultado);
    }

    @Test
    void agregarProducto_ok() {

        Usuario usuario = usuario();

        Carrito carrito = Carrito.builder()
                .id(1L)
                .coste(0.0)
                .build();

        PostVenta post = PostVenta.builder()
                .id(10L)
                .precio(20.0)
                .build();

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        when(carritoRepository.findByUsuarioId(1L))
                .thenReturn(Optional.of(carrito));

        when(postVentaRepository.findById(10L))
                .thenReturn(Optional.of(post));

        when(productoCarritoRepository
                .existsByCarritoIdAndPostVentaId(1L, 10L))
                .thenReturn(false);

        when(productoCarritoRepository
                .findByCarritoIdAndPostVentaId(1L, 10L))
                .thenReturn(Optional.empty());

        service.agregarProducto(10L);

        verify(productoCarritoRepository)
                .save(any(ProductoCarrito.class));

        verify(carritoRepository)
                .save(carrito);

        assertEquals(20.0, carrito.getCoste());
    }

    @Test
    void agregarProducto_creaCarritoSiNoExiste() {

        Usuario usuario = usuario();

        Carrito carrito = Carrito.builder()
                .id(1L)
                .coste(0.0)
                .build();

        PostVenta post = PostVenta.builder()
                .id(10L)
                .precio(10.0)
                .build();

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        when(carritoRepository.findByUsuarioId(1L))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(carrito));

        when(postVentaRepository.findById(10L))
                .thenReturn(Optional.of(post));

        when(productoCarritoRepository
                .existsByCarritoIdAndPostVentaId(anyLong(), anyLong()))
                .thenReturn(false);

        when(productoCarritoRepository
                .findByCarritoIdAndPostVentaId(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        service.agregarProducto(10L);

        verify(service).create();
    }

    @Test
    void agregarProducto_productoYaExiste_exception() {

        Usuario usuario = usuario();

        Carrito carrito = Carrito.builder()
                .id(1L)
                .build();

        PostVenta post = PostVenta.builder()
                .id(10L)
                .precio(10.0)
                .build();

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        when(carritoRepository.findByUsuarioId(1L))
                .thenReturn(Optional.of(carrito));

        when(postVentaRepository.findById(10L))
                .thenReturn(Optional.of(post));

        when(productoCarritoRepository
                .existsByCarritoIdAndPostVentaId(1L, 10L))
                .thenReturn(true);

        assertThrows(
                GSBadRequestException.class,
                () -> service.agregarProducto(10L)
        );
    }

    @Test
    void eliminarProducto_ok() {

        Usuario usuario = usuario();

        Carrito carrito = Carrito.builder()
                .id(1L)
                .coste(50.0)
                .build();

        PostVenta post = PostVenta.builder()
                .precio(20.0)
                .build();

        ProductoCarrito pc = ProductoCarrito.builder()
                .postVenta(post)
                .build();

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        when(carritoRepository.findByUsuarioId(1L))
                .thenReturn(Optional.of(carrito));

        when(productoCarritoRepository
                .findByCarritoIdAndPostVentaId(1L, 5L))
                .thenReturn(Optional.of(pc));

        service.eliminarProducto(5L);

        assertEquals(30.0, carrito.getCoste());

        verify(productoCarritoRepository)
                .delete(pc);
    }

    @Test
    void eliminarProducto_notFound() {

        Usuario usuario = usuario();

        Carrito carrito = Carrito.builder()
                .id(1L)
                .build();

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        when(carritoRepository.findByUsuarioId(1L))
                .thenReturn(Optional.of(carrito));

        when(productoCarritoRepository
                .findByCarritoIdAndPostVentaId(1L, 5L))
                .thenReturn(Optional.empty());

        assertThrows(
                GSNotFoundException.class,
                () -> service.eliminarProducto(5L)
        );
    }

    @Test
    void vaciar_ok() {

        Usuario usuario = usuario();

        Carrito carrito = Carrito.builder()
                .id(1L)
                .build();

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        when(carritoRepository.findByUsuarioId(1L))
                .thenReturn(Optional.of(carrito));

        service.vaciar();

        verify(productoCarritoRepository)
                .deleteAllByCarritoId(1L);
    }

    @Test
    void estaEnCarrito() {

        Usuario usuario = usuario();

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        when(productoCarritoRepository
                .existsByCarritoIdAndPostVentaId(1L, 5L))
                .thenReturn(true);

        assertTrue(service.estaEnCarrito(5L));
    }

    @Test
    void getPrecioCarrito2() {

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Carrito carrito = new Carrito();
        carrito.setId(1L);
        carrito.setCoste(50.0);

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        when(carritoRepository.findByUsuarioId(1L))
                .thenReturn(Optional.of(carrito));

        Double resultado = service.getPrecioCarrito();

        assertEquals(50.0, resultado);
    }

    @Test
    void getCarrito() {

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setIdAPI(100);
        producto.setNombre("FIFA");
        producto.setEstado(EstadoProducto.NUEVO);

        Usuario vendedor = new Usuario();
        vendedor.setId(2L);
        vendedor.setNombreUsuario("angel");

        PostVenta venta = new PostVenta();
        venta.setId(10L);
        venta.setProducto(producto);
        venta.setVendedor(vendedor);
        venta.setPrecio(25.0);
        venta.setDescripcion("desc");
        venta.setPlataforma("PS5");

        Carrito carrito = new Carrito();
        carrito.setId(1L);

        ProductoCarrito pc = new ProductoCarrito();
        pc.setId(1L);
        pc.setCarrito(carrito);
        pc.setPostVenta(venta);

        carrito.setProductos(List.of(pc));

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        when(carritoRepository.findByUsuarioId(1L))
                .thenReturn(Optional.of(carrito));

        when(carritoRepository.save(any(Carrito.class)))
                .thenAnswer(i -> i.getArgument(0));

        when(productoCarritoRepository.findByCarritoId(1L))
                .thenReturn(List.of(pc));

        List<ProductoCarritoResponseDTO> resultado =
                service.getCarrito();

        assertEquals(1, resultado.size());

        verify(carritoRepository).save(any(Carrito.class));
    }
}