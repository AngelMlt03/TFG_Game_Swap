package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.response.CompraVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.IntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.*;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import com.tfg.angel.gameswap.backend.business.repository.CompraVentaRepository;
import com.tfg.angel.gameswap.backend.business.repository.IntercambioRepository;
import com.tfg.angel.gameswap.backend.business.service.impl.HistorialService;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistorialServiceTest {

    @Mock
    private CompraVentaRepository compraVentaRepository;

    @Mock
    private IntercambioRepository intercambioRepository;

    @Mock
    private UsuarioDetailsService usuarioDetailsService;

    @InjectMocks
    private HistorialService historialService;

    private Usuario usuario(Long id) {
        Usuario u = new Usuario();
        u.setId(id);
        u.setNombreUsuario("user" + id);
        u.setNombre("Usuario " + id);
        return u;
    }

    private Producto producto(Long id) {
        Producto p = new Producto();
        p.setId(id);
        p.setIdAPI(id.intValue());
        p.setEstado(EstadoProducto.NUEVO);
        return p;
    }

    private CompraVenta compraVenta(Long id, Usuario comprador, Usuario vendedor) {

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setIdAPI(100);
        producto.setNombre("Zelda");
        producto.setEstado(EstadoProducto.NUEVO);

        PostVenta post = new PostVenta();
        post.setId(100L);
        post.setVendedor(vendedor);
        post.setProducto(producto);
        post.setPlataforma("Switch");
        post.setPrecio(50.0);

        CompraVenta compra = new CompraVenta();
        compra.setId(id);
        compra.setComprador(comprador);
        compra.setPostVenta(post);
        compra.setPrecio(50.0);

        return compra;
    }

    private Intercambio intercambio(Long id, Usuario publicador, Usuario usuarioCambio) {

        PostIntercambio post = new PostIntercambio();
        post.setId(200L);
        post.setUsuario(publicador);
        post.setProducto(producto(1L));
        post.setProductoCambio(producto(2L));

        Intercambio intercambio = new Intercambio();
        intercambio.setId(id);
        intercambio.setPostIntercambio(post);
        intercambio.setUsuarioCambio(usuarioCambio);

        return intercambio;
    }

    @Test
    void getHistorialCompras() {

        Usuario comprador = usuario(1L);
        Usuario vendedor = usuario(2L);

        CompraVenta compra = compraVenta(1L, comprador, vendedor);

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(comprador);

        when(compraVentaRepository.findByCompradorId(1L))
                .thenReturn(List.of(compra));

        List<CompraVentaResponseDTO> resultado =
                historialService.getHistorialCompras();

        assertEquals(1, resultado.size());
    }

    @Test
    void getHistorialVentas() {

        Usuario vendedor = usuario(1L);
        Usuario comprador = usuario(2L);

        CompraVenta valida =
                compraVenta(1L, comprador, vendedor);

        CompraVenta noValida =
                compraVenta(2L, comprador, usuario(99L));

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(vendedor);

        when(compraVentaRepository.findAll())
                .thenReturn(List.of(valida, noValida));

        List<CompraVentaResponseDTO> resultado =
                historialService.getHistorialVentas();

        assertEquals(1, resultado.size());
    }

    @Test
    void getHistorialIntercambios_usuarioPublicador() {

        Usuario usuario = usuario(1L);

        Intercambio intercambio =
                intercambio(1L, usuario, usuario(2L));

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        when(intercambioRepository.findAll())
                .thenReturn(List.of(intercambio));

        List<IntercambioResponseDTO> resultado =
                historialService.getHistorialIntercambios();

        assertEquals(1, resultado.size());
    }

    @Test
    void getHistorialIntercambios_usuarioCambio() {

        Usuario usuario = usuario(1L);

        Intercambio intercambio =
                intercambio(1L, usuario(2L), usuario);

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        when(intercambioRepository.findAll())
                .thenReturn(List.of(intercambio));

        List<IntercambioResponseDTO> resultado =
                historialService.getHistorialIntercambios();

        assertEquals(1, resultado.size());
    }

    @Test
    void getHistorialIntercambios_noPerteneceAlUsuario() {

        Usuario usuario = usuario(1L);

        Intercambio intercambio =
                intercambio(1L, usuario(2L), usuario(3L));

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        when(intercambioRepository.findAll())
                .thenReturn(List.of(intercambio));

        List<IntercambioResponseDTO> resultado =
                historialService.getHistorialIntercambios();

        assertTrue(resultado.isEmpty());
    }

    @Test
    void getCompra() {

        CompraVenta compra =
                compraVenta(1L, usuario(1L), usuario(2L));

        when(compraVentaRepository.findById(1L))
                .thenReturn(Optional.of(compra));

        CompraVentaResponseDTO dto =
                historialService.getCompra(1L);

        assertNotNull(dto);
    }

    @Test
    void getIntercambio() {

        Intercambio intercambio =
                intercambio(1L, usuario(1L), usuario(2L));

        when(intercambioRepository.findById(1L))
                .thenReturn(Optional.of(intercambio));

        IntercambioResponseDTO dto =
                historialService.getIntercambio(1L);

        assertNotNull(dto);
    }
}
