package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.request.GuardadoRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostIntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.*;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import com.tfg.angel.gameswap.backend.business.repository.GuardadoRepository;
import com.tfg.angel.gameswap.backend.business.repository.PostIntercambioRepository;
import com.tfg.angel.gameswap.backend.business.repository.PostVentaRepository;
import com.tfg.angel.gameswap.backend.business.service.impl.GuardadoServiceImpl;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
import org.junit.jupiter.api.BeforeEach;
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
class GuardadoServiceTest {

    @Mock
    private GuardadoRepository guardadoRepository;

    @Mock
    private PostVentaRepository postVentaRepository;

    @Mock
    private PostIntercambioRepository postIntercambioRepository;

    @Mock
    private UsuarioDetailsService usuarioDetailsService;

    @InjectMocks
    private GuardadoServiceImpl service;

    @BeforeEach
    void setUp() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .build();

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);
    }

    @Test
    void guardar_debeGuardarCuandoNoExiste() {

        GuardadoRequestDTO dto = new GuardadoRequestDTO();
        dto.setIdPost(10L);
        dto.setTipoPost("VENTA");

        when(guardadoRepository.existsByIdUsuarioAndIdPostAndTipoPost(
                1L,
                10L,
                "VENTA"
        )).thenReturn(false);

        service.guardar(dto);

        verify(guardadoRepository).save(any(Guardado.class));
    }

    @Test
    void guardar_noDebeGuardarCuandoYaExiste() {

        GuardadoRequestDTO dto = new GuardadoRequestDTO();
        dto.setIdPost(10L);
        dto.setTipoPost("VENTA");

        when(guardadoRepository.existsByIdUsuarioAndIdPostAndTipoPost(
                1L,
                10L,
                "VENTA"
        )).thenReturn(true);

        service.guardar(dto);

        verify(guardadoRepository, never()).save(any());
    }

    @Test
    void eliminar_debeEliminarCuandoExiste() {

        Guardado guardado = Guardado.builder()
                .id(1L)
                .idUsuario(1L)
                .idPost(10L)
                .tipoPost("VENTA")
                .build();

        when(
                guardadoRepository.findByIdUsuarioAndIdPostAndTipoPost(
                        1L,
                        10L,
                        "VENTA"
                )
        ).thenReturn(Optional.of(guardado));

        service.eliminar(10L, "VENTA");

        verify(guardadoRepository).delete(guardado);
    }

    @Test
    void eliminar_noDebeFallarCuandoNoExiste() {

        when(
                guardadoRepository.findByIdUsuarioAndIdPostAndTipoPost(
                        1L,
                        10L,
                        "VENTA"
                )
        ).thenReturn(Optional.empty());

        service.eliminar(10L, "VENTA");

        verify(guardadoRepository, never()).delete(any());
    }

    @Test
    void existe_debeRetornarTrue() {

        when(
                guardadoRepository.existsByIdUsuarioAndIdPostAndTipoPost(
                        1L,
                        10L,
                        "VENTA"
                )
        ).thenReturn(true);

        assertTrue(
                service.existe(10L, "VENTA")
        );
    }

    @Test
    void existe_debeRetornarFalse() {

        when(
                guardadoRepository.existsByIdUsuarioAndIdPostAndTipoPost(
                        1L,
                        10L,
                        "VENTA"
                )
        ).thenReturn(false);

        assertFalse(
                service.existe(10L, "VENTA")
        );
    }

    @Test
    void getVentasGuardadas() {

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Guardado guardado = Guardado.builder()
                .idPost(10L)
                .tipoPost("VENTA")
                .build();

        Usuario vendedor = new Usuario();
        vendedor.setId(2L);
        vendedor.setNombreUsuario("vendedor");

        Producto producto = new Producto();
        producto.setId(100L);
        producto.setIdAPI(123);
        producto.setNombre("FIFA");
        producto.setEstado(EstadoProducto.NUEVO);

        PostVenta venta = new PostVenta();
        venta.setId(10L);
        venta.setVendedor(vendedor);
        venta.setProducto(producto);
        venta.setPlataforma("PS5");
        venta.setPrecio(20.0);
        venta.setDescripcion("desc");

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        when(guardadoRepository.findByIdUsuarioAndTipoPost(1L, "VENTA"))
                .thenReturn(List.of(guardado));

        when(postVentaRepository.findAllById(List.of(10L)))
                .thenReturn(List.of(venta));

        List<PostVentaResponseDTO> resultado =
                service.getVentasGuardadas();

        assertEquals(1, resultado.size());
        assertEquals("FIFA", resultado.getFirst().getNombreProducto());
    }

    @Test
    void getIntercambiosGuardados() {

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Guardado guardado = Guardado.builder()
                .idPost(20L)
                .tipoPost("INTERCAMBIO")
                .build();

        Usuario publicador = new Usuario();
        publicador.setId(2L);
        publicador.setNombreUsuario("angel");

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setIdAPI(111);
        producto.setNombre("FIFA");
        producto.setEstado(EstadoProducto.NUEVO);

        Producto productoCambio = new Producto();
        productoCambio.setId(2L);
        productoCambio.setIdAPI(222);
        productoCambio.setNombre("COD");
        productoCambio.setEstado(EstadoProducto.USADO);

        PostIntercambio intercambio = new PostIntercambio();
        intercambio.setId(20L);
        intercambio.setUsuario(publicador);
        intercambio.setProducto(producto);
        intercambio.setProductoCambio(productoCambio);
        intercambio.setPlataforma("PS5");
        intercambio.setPlataformaCambio("XBOX");
        intercambio.setDescripcion("desc");

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        when(guardadoRepository.findByIdUsuarioAndTipoPost(1L, "INTERCAMBIO"))
                .thenReturn(List.of(guardado));

        when(postIntercambioRepository.findAllById(List.of(20L)))
                .thenReturn(List.of(intercambio));

        List<PostIntercambioResponseDTO> resultado =
                service.getIntercambiosGuardados();

        assertEquals(1, resultado.size());
        assertEquals("FIFA", resultado.getFirst().getNombreProducto());
    }
}
