package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.response.*;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import com.tfg.angel.gameswap.backend.business.service.PostIntercambioService;
import com.tfg.angel.gameswap.backend.business.service.PostVentaService;
import com.tfg.angel.gameswap.backend.business.service.ProductoService;
import com.tfg.angel.gameswap.backend.business.service.impl.BusquedaService;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusquedaServiceTest {

    @Mock
    private PostVentaService postVentaService;

    @Mock
    private PostIntercambioService postIntercambioService;

    @Mock
    private ProductoService productoService;

    @Mock
    private UsuarioDetailsService usuarioDetailsService;

    @InjectMocks
    private BusquedaService service;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .build();
    }

    @Test
    void buscar_debeRetornarVentas() {

        when(usuarioDetailsService.obtenerUsuarioActual()).thenReturn(usuario);

        PostVentaResponseDTO venta = PostVentaResponseDTO.builder()
                .id(10L)
                .idVendedor(2L)
                .idProducto(100L)
                .nombreProducto("God of War")
                .plataforma("PS5")
                .precio(30.0)
                .nombreUsuario("vendedor")
                .descripcion("desc")
                .build();

        ProductoResponseDTO producto = ProductoResponseDTO.builder()
                .id(100L)
                .idAPI(500)
                .estado(EstadoProducto.NUEVO)
                .build();

        when(postVentaService.findByEstado(EstadoPost.ACTIVO))
                .thenReturn(List.of(venta));

        when(productoService.findById(100L))
                .thenReturn(producto);

        List<PostBusquedaDTO> resultado =
                service.buscar(null, "VENTA", null, null);

        assertEquals(1, resultado.size());
        assertEquals("VENTA", resultado.getFirst().getTipo());

        verify(postVentaService).findByEstado(EstadoPost.ACTIVO);
    }

    @Test
    void buscar_debeRetornarIntercambios() {

        when(usuarioDetailsService.obtenerUsuarioActual()).thenReturn(usuario);

        PostIntercambioResponseDTO intercambio =
                PostIntercambioResponseDTO.builder()
                        .id(20L)
                        .idUsuario(2L)
                        .idProducto(200L)
                        .idProductoIntercambio(201L)
                        .nombreProducto("Zelda")
                        .nombreProductoIntercambio("Mario")
                        .plataforma("Switch")
                        .plataformaIntercambio("Switch")
                        .nombreUsuario("usuario")
                        .descripcion("desc")
                        .build();

        ProductoResponseDTO producto = ProductoResponseDTO.builder()
                .id(200L)
                .idAPI(1000)
                .estado(EstadoProducto.USADO)
                .build();

        ProductoResponseDTO productoCambio = ProductoResponseDTO.builder()
                .id(201L)
                .idAPI(1001)
                .estado(EstadoProducto.NUEVO)
                .build();

        when(postIntercambioService.findByEstado(EstadoPost.ACTIVO))
                .thenReturn(List.of(intercambio));

        when(productoService.findById(200L))
                .thenReturn(producto);

        when(productoService.findById(201L))
                .thenReturn(productoCambio);

        List<PostBusquedaDTO> resultado =
                service.buscar(null, "INTERCAMBIO", null, null);

        assertEquals(1, resultado.size());
        assertEquals("INTERCAMBIO", resultado.getFirst().getTipo());

        verify(postIntercambioService).findByEstado(EstadoPost.ACTIVO);
    }

    @Test
    void buscar_conTipoNull_debeRetornarVentasEIntercambios() {

        when(usuarioDetailsService.obtenerUsuarioActual()).thenReturn(usuario);

        PostVentaResponseDTO venta = PostVentaResponseDTO.builder()
                .id(1L)
                .idVendedor(2L)
                .idProducto(10L)
                .nombreProducto("Fifa")
                .plataforma("PS5")
                .precio(20.0)
                .build();

        PostIntercambioResponseDTO intercambio =
                PostIntercambioResponseDTO.builder()
                        .id(2L)
                        .idUsuario(3L)
                        .idProducto(20L)
                        .idProductoIntercambio(21L)
                        .nombreProducto("Halo")
                        .nombreProductoIntercambio("Gears")
                        .plataforma("Xbox")
                        .plataformaIntercambio("Xbox")
                        .build();

        when(postVentaService.findByEstado(EstadoPost.ACTIVO))
                .thenReturn(List.of(venta));

        when(postIntercambioService.findByEstado(EstadoPost.ACTIVO))
                .thenReturn(List.of(intercambio));

        when(productoService.findById(10L))
                .thenReturn(
                        ProductoResponseDTO.builder()
                                .idAPI(10)
                                .estado(EstadoProducto.NUEVO)
                                .build()
                );

        when(productoService.findById(20L))
                .thenReturn(
                        ProductoResponseDTO.builder()
                                .idAPI(20)
                                .estado(EstadoProducto.USADO)
                                .build()
                );

        when(productoService.findById(21L))
                .thenReturn(
                        ProductoResponseDTO.builder()
                                .idAPI(21)
                                .estado(EstadoProducto.NUEVO)
                                .build()
                );

        List<PostBusquedaDTO> resultado =
                service.buscar(null, null, null, null);

        assertEquals(2, resultado.size());
    }

    @Test
    void buscar_noDebeMostrarPublicacionesDelUsuarioActual() {

        when(usuarioDetailsService.obtenerUsuarioActual()).thenReturn(usuario);

        PostVentaResponseDTO ventaPropia = PostVentaResponseDTO.builder()
                .id(1L)
                .idVendedor(1L)
                .idProducto(10L)
                .nombreProducto("Juego")
                .build();

        when(postVentaService.findByEstado(EstadoPost.ACTIVO))
                .thenReturn(List.of(ventaPropia));

        List<PostBusquedaDTO> resultado =
                service.buscar(null, "VENTA", null, null);

        assertTrue(resultado.isEmpty());
    }
}