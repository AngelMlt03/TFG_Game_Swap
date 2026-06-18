package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.model.*;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import com.tfg.angel.gameswap.backend.business.repository.*;
import com.tfg.angel.gameswap.backend.business.service.CarritoService;
import com.tfg.angel.gameswap.backend.business.service.impl.TransaccionService;
import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceTest {

    @Mock
    private PostVentaRepository postVentaRepository;
    @Mock
    private PostIntercambioRepository postIntercambioRepository;
    @Mock
    private GuardadoRepository guardadoRepository;
    @Mock
    private CompraVentaRepository compraVentaRepository;
    @Mock
    private IntercambioRepository intercambioRepository;
    @Mock
    private ProductoCarritoRepository productoCarritoRepository;
    @Mock
    private CarritoRepository carritoRepository;
    @Mock
    private UsuarioDetailsService usuarioDetailsService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private CarritoService carritoService;

    @InjectMocks
    private TransaccionService service;

    private Usuario usuario(Long id, Double saldo) {
        Usuario u = new Usuario();
        u.setId(id);
        u.setSaldo(saldo);
        u.setNombreUsuario("user" + id);
        return u;
    }

    private PostVenta crearVenta() {

        Usuario vendedor = usuario(2L, 50.0);

        PostVenta venta = new PostVenta();
        venta.setId(1L);
        venta.setVendedor(vendedor);
        venta.setPrecio(20.0);
        venta.setEstado(EstadoPost.ACTIVO);

        return venta;
    }

    @Test
    void comprarCorrectamente() {

        Usuario comprador = usuario(1L, 100.0);

        PostVenta venta = crearVenta();

        Carrito carrito = new Carrito();
        carrito.setId(10L);

        when(postVentaRepository.findById(1L))
                .thenReturn(Optional.of(venta));

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(comprador);

        when(guardadoRepository.findByIdUsuarioAndIdPost(1L, 1L))
                .thenReturn(Optional.empty());

        when(carritoRepository.findByUsuarioId(1L))
                .thenReturn(Optional.of(carrito));

        when(productoCarritoRepository.existsByCarritoIdAndPostVentaId(10L, 1L))
                .thenReturn(false);

        Double saldo = service.comprar(1L);

        assertEquals(80.0, saldo);

        verify(compraVentaRepository).save(any(CompraVenta.class));
        verify(postVentaRepository).save(venta);
        verify(usuarioRepository, times(2)).save(any());
    }

    @Test
    void comprarEliminaGuardadoYCarrito() {

        Usuario comprador = usuario(1L, 100.0);

        PostVenta venta = crearVenta();

        Guardado guardado = new Guardado();
        guardado.setId(99L);

        Carrito carrito = new Carrito();
        carrito.setId(10L);

        when(postVentaRepository.findById(1L))
                .thenReturn(Optional.of(venta));

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(comprador);

        when(guardadoRepository.findByIdUsuarioAndIdPost(1L, 1L))
                .thenReturn(Optional.of(guardado));

        when(carritoRepository.findByUsuarioId(1L))
                .thenReturn(Optional.of(carrito));

        when(productoCarritoRepository.existsByCarritoIdAndPostVentaId(10L, 1L))
                .thenReturn(true);

        service.comprar(1L);

        verify(guardadoRepository).deleteById(99L);
        verify(carritoService).eliminarProducto(1L);
    }

    @Test
    void comprarProductoPropio() {

        Usuario usuario = usuario(2L, 100.0);

        PostVenta venta = crearVenta();

        when(postVentaRepository.findById(1L))
                .thenReturn(Optional.of(venta));

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        assertThrows(
                GSBadRequestException.class,
                () -> service.comprar(1L)
        );
    }

    @Test
    void comprarProductoFinalizado() {

        Usuario comprador = usuario(1L, 100.0);

        PostVenta venta = crearVenta();
        venta.setEstado(EstadoPost.FINALIZADO);

        when(postVentaRepository.findById(1L))
                .thenReturn(Optional.of(venta));

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(comprador);

        assertThrows(
                GSBadRequestException.class,
                () -> service.comprar(1L)
        );
    }

    @Test
    void comprarSaldoInsuficiente() {

        Usuario comprador = usuario(1L, 5.0);

        PostVenta venta = crearVenta();

        when(postVentaRepository.findById(1L))
                .thenReturn(Optional.of(venta));

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(comprador);

        assertThrows(
                GSBadRequestException.class,
                () -> service.comprar(1L)
        );
    }

    @Test
    void comprarPostNoEncontrado() {

        when(postVentaRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                GSNotFoundException.class,
                () -> service.comprar(1L)
        );
    }

    @Test
    void intercambiarCorrectamente() {

        Usuario propietario = usuario(2L, 0.0);
        Usuario usuarioActual = usuario(1L, 0.0);

        PostIntercambio post = new PostIntercambio();
        post.setId(1L);
        post.setUsuario(propietario);
        post.setEstado(EstadoPost.ACTIVO);

        when(postIntercambioRepository.findById(1L))
                .thenReturn(Optional.of(post));

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuarioActual);

        service.intercambiar(1L);

        assertEquals(
                EstadoPost.FINALIZADO,
                post.getEstado()
        );

        verify(postIntercambioRepository).save(post);
        verify(intercambioRepository)
                .save(any(Intercambio.class));
    }

    @Test
    void intercambiarConsigoMismo() {

        Usuario usuario = usuario(1L, 0.0);

        PostIntercambio post = new PostIntercambio();
        post.setUsuario(usuario);

        when(postIntercambioRepository.findById(1L))
                .thenReturn(Optional.of(post));

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        assertThrows(
                GSBadRequestException.class,
                () -> service.intercambiar(1L)
        );
    }

    @Test
    void intercambiarPostNoEncontrado() {

        when(postIntercambioRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                GSNotFoundException.class,
                () -> service.intercambiar(1L)
        );
    }
}
