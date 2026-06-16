package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.response.CompraVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.CompraVenta;
import com.tfg.angel.gameswap.backend.business.model.PostVenta;
import com.tfg.angel.gameswap.backend.business.model.Producto;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import com.tfg.angel.gameswap.backend.business.model.enums.Rol;
import com.tfg.angel.gameswap.backend.business.repository.CompraVentaRepository;
import com.tfg.angel.gameswap.backend.business.repository.PostVentaRepository;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.impl.CompraVentaServiceImpl;
import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
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
class CompraVentaServiceTest {

    @Mock
    private CompraVentaRepository compraVentaRepository;
    @Mock
    private PostVentaRepository postVentaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private UsuarioDetailsService usuarioDetailsService;

    @InjectMocks
    private CompraVentaServiceImpl compraVentaService;

    private CompraVenta compraVenta;
    private Usuario comprador;
    private Usuario vendedor;
    private PostVenta postVenta;

    @BeforeEach
    void setUp() {

        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Elden Ring")
                .estado(EstadoProducto.NUEVO)
                .build();

        comprador = Usuario.builder()
                .id(1L)
                .nombre("Comprador")
                .rol(Rol.CLIENTE)
                .saldo(100.0)
                .build();

        vendedor = Usuario.builder()
                .id(2L)
                .nombre("Vendedor")
                .rol(Rol.CLIENTE)
                .saldo(50.0)
                .build();

        postVenta = PostVenta.builder()
                .id(10L)
                .vendedor(vendedor)
                .producto(producto)
                .precio(40.0)
                .estado(EstadoPost.ACTIVO)
                .build();

        compraVenta = CompraVenta.builder()
                .id(100L)
                .comprador(comprador)
                .postVenta(postVenta)
                .precio(50.0)
                .build();
    }

    @Test
    @DisplayName("Debe eliminar una transacción si existe")
    void delete_Success() {

        when(compraVentaRepository.existsById(100L)).thenReturn(true);

        compraVentaService.delete(100L);

        verify(compraVentaRepository).deleteById(100L);
    }

    @Test
    @DisplayName("Debe lanzar GSNotFoundException al intentar borrar una transacción inexistente")
    void delete_ThrowsNotFound() {

        when(compraVentaRepository.existsById(100L)).thenReturn(false);

        assertThrows(GSNotFoundException.class, () ->
                compraVentaService.delete(100L)
        );
    }


    @Test
    @DisplayName("Debe lanzar GSNotFoundException si no existe")
    void findById_NotFound() {

        when(compraVentaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(GSNotFoundException.class, () -> compraVentaService.findById(999L));
    }

    private Usuario crearUsuario() {
        return Usuario.builder()
                .id(1L)
                .nombre("Angel")
                .build();
    }

    @Test
    void create_usuarioNoEncontrado() {

        Usuario usuario = crearUsuario();

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                GSNotFoundException.class,
                () -> compraVentaService.create(1L, 1L)
        );
    }

    @Test
    void create_postVentaNoEncontrado() {

        Usuario usuario = crearUsuario();

        when(usuarioDetailsService.obtenerUsuarioActual())
                .thenReturn(usuario);

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(postVentaRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                GSNotFoundException.class,
                () -> compraVentaService.create(1L, 1L)
        );
    }

    @Test
    void findById_notFound() {

        when(compraVentaRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                GSNotFoundException.class,
                () -> compraVentaService.findById(1L)
        );
    }

    @Test
    void delete_ok() {

        when(compraVentaRepository.existsById(1L))
                .thenReturn(true);

        compraVentaService.delete(1L);

        verify(compraVentaRepository)
                .deleteById(1L);
    }

    @Test
    void delete_notFound() {

        when(compraVentaRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(
                GSNotFoundException.class,
                () -> compraVentaService.delete(1L)
        );
    }
}
