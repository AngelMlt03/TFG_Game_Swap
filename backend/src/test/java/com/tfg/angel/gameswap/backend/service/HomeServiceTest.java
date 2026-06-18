package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.response.HomeStatsDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostBusquedaDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.UsuarioRankingDTO;
import com.tfg.angel.gameswap.backend.business.model.PostIntercambio;
import com.tfg.angel.gameswap.backend.business.model.PostVenta;
import com.tfg.angel.gameswap.backend.business.model.Producto;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import com.tfg.angel.gameswap.backend.business.repository.PostIntercambioRepository;
import com.tfg.angel.gameswap.backend.business.repository.PostVentaRepository;
import com.tfg.angel.gameswap.backend.business.repository.ReviewRepository;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.impl.HomeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HomeServiceTest {

    @Mock
    private PostVentaRepository postventaRepository;

    @Mock
    private PostIntercambioRepository postintercambioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private HomeServiceImpl service;

    private Usuario crearUsuario(Long id, String nombre, Double estrellas) {
        Usuario u = new Usuario();
        u.setId(id);
        u.setNombre(nombre);
        u.setNombreUsuario(nombre);
        u.setCorreo(nombre + "@mail.com");
        u.setEstrellas(estrellas);
        return u;
    }

    private Producto crearProducto(Long id, String nombre) {
        Producto p = new Producto();
        p.setId(id);
        p.setIdAPI(id.intValue());
        p.setNombre(nombre);
        p.setEstado(EstadoProducto.NUEVO);
        return p;
    }

    @Test
    void ultimasVentas() {

        Usuario vendedor = crearUsuario(1L, "angel", 5.0);

        Producto producto = crearProducto(10L, "Zelda");

        PostVenta venta = new PostVenta();
        venta.setId(1L);
        venta.setVendedor(vendedor);
        venta.setProducto(producto);
        venta.setPlataforma("Switch");
        venta.setPrecio(40.0);
        venta.setDescripcion("descripcion");

        when(postventaRepository.findTop4ByEstadoOrderByIdDesc(EstadoPost.ACTIVO))
                .thenReturn(List.of(venta));

        List<PostBusquedaDTO> resultado = service.ultimasVentas();

        assertEquals(1, resultado.size());
        assertEquals("VENTA", resultado.getFirst().getTipo());
        assertEquals("Zelda", resultado.getFirst().getNombreProducto());
    }

    @Test
    void ultimosIntercambios() {

        Usuario usuario = crearUsuario(1L, "angel", 5.0);

        Producto producto = crearProducto(1L, "Zelda");
        Producto productoCambio = crearProducto(2L, "Mario");

        PostIntercambio intercambio = new PostIntercambio();
        intercambio.setId(1L);
        intercambio.setUsuario(usuario);
        intercambio.setProducto(producto);
        intercambio.setProductoCambio(productoCambio);
        intercambio.setPlataforma("Switch");
        intercambio.setPlataformaCambio("PS5");
        intercambio.setDescripcion("descripcion");

        when(postintercambioRepository.findTop4ByEstadoOrderByIdDesc(EstadoPost.ACTIVO))
                .thenReturn(List.of(intercambio));

        List<PostBusquedaDTO> resultado = service.ultimosIntercambios();

        assertEquals(1, resultado.size());
        assertEquals("INTERCAMBIO", resultado.getFirst().getTipo());
        assertEquals("Mario", resultado.getFirst().getNombreProductoIntercambio());
    }

    @Test
    void estadisticas() {

        PostVenta venta = new PostVenta();
        PostIntercambio intercambio = new PostIntercambio();

        when(postventaRepository.findByEstado(EstadoPost.ACTIVO))
                .thenReturn(List.of(venta, venta));

        when(postintercambioRepository.findByEstado(EstadoPost.ACTIVO))
                .thenReturn(List.of(intercambio));

        when(usuarioRepository.count())
                .thenReturn(15L);

        when(reviewRepository.count())
                .thenReturn(20L);

        HomeStatsDTO dto = service.estadisticas();

        assertEquals(2L, dto.getVentas());
        assertEquals(1L, dto.getIntercambios());
        assertEquals(15L, dto.getUsuarios());
        assertEquals(20L, dto.getReviews());
    }

    @Test
    void topUsuarios() {

        Usuario u1 = crearUsuario(1L, "u1", 5.0);
        Usuario u2 = crearUsuario(2L, "u2", 2.0);
        Usuario u3 = crearUsuario(3L, "u3", 4.0);
        Usuario u4 = crearUsuario(4L, "u4", null);

        when(usuarioRepository.findAll())
                .thenReturn(List.of(u2, u1, u4, u3));

        List<UsuarioRankingDTO> resultado = service.topUsuarios();

        assertEquals(4, resultado.size());
    }

    @Test
    void topUsuarios_limite6() {

        when(usuarioRepository.findAll())
                .thenReturn(List.of(
                        crearUsuario(1L, "u1", 1.0),
                        crearUsuario(2L, "u2", 2.0),
                        crearUsuario(3L, "u3", 3.0),
                        crearUsuario(4L, "u4", 4.0),
                        crearUsuario(5L, "u5", 5.0),
                        crearUsuario(6L, "u6", 6.0),
                        crearUsuario(7L, "u7", 7.0)
                ));

        List<UsuarioRankingDTO> resultado = service.topUsuarios();

        assertEquals(6, resultado.size());
        assertEquals("u7", resultado.getFirst().getNombreUsuario());
    }
}