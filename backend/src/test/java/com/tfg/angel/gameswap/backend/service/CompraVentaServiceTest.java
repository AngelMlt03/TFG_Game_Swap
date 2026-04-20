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
    @DisplayName("Debe realizar la compra correctamente actualizando saldos y estado del post")
    void create_Success() {

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(comprador));
        when(postVentaRepository.findById(10L)).thenReturn(Optional.of(postVenta));

        CompraVenta cvGuardada = CompraVenta.builder()
                .id(100L)
                .comprador(comprador)
                .postVenta(postVenta)
                .precio(40.0)
                .build();

        when(compraVentaRepository.save(any(CompraVenta.class))).thenReturn(cvGuardada);

        CompraVentaResponseDTO response = compraVentaService.create(10L, 1L);

        assertNotNull(response);
        assertEquals(60.0, comprador.getSaldo());
        assertEquals(90.0, vendedor.getSaldo());
        assertEquals(EstadoPost.FINALIZADO, postVenta.getEstado());

        verify(postVentaRepository).save(postVenta);
        verify(compraVentaRepository).save(any(CompraVenta.class));
    }

    @Test
    @DisplayName("Debe lanzar GSBadRequestException si el comprador no tiene saldo suficiente")
    void create_ThrowsBadRequest_WhenInsufficientBalance() {

        comprador.setSaldo(10.0);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(comprador));
        when(postVentaRepository.findById(10L)).thenReturn(Optional.of(postVenta));

        GSBadRequestException ex = assertThrows(GSBadRequestException.class, () ->
                compraVentaService.create(10L, 1L)
        );

        assertEquals("Saldo insuficiente", ex.getMessage());
        verify(compraVentaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar GSBadRequestException si un usuario intenta comprar su propio producto")
    void create_ThrowsBadRequest_WhenSameUser() {

        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(vendedor));
        when(postVentaRepository.findById(10L)).thenReturn(Optional.of(postVenta));

        assertThrows(GSBadRequestException.class, () ->
                compraVentaService.create(10L, 2L)
        );
    }

    @Test
    @DisplayName("Debe lanzar GSNotFoundException si el PostVenta no existe")
    void create_ThrowsNotFound_WhenPostMissing() {

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(comprador));
        when(postVentaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(GSNotFoundException.class, () ->
                compraVentaService.create(99L, 1L)
        );
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
    @DisplayName("Debe devolver una lista con todas las transacciones")
    void findAll_Success() {

        when(compraVentaRepository.findAll()).thenReturn(List.of(compraVenta, compraVenta));

        List<CompraVentaResponseDTO> result = compraVentaService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(compraVentaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe devolver el DTO si la transacción existe")
    void findById_Success() {

        when(compraVentaRepository.findById(100L)).thenReturn(Optional.of(compraVenta));

        CompraVentaResponseDTO result = compraVentaService.findById(100L);

        assertNotNull(result);
        verify(compraVentaRepository).findById(100L);
    }

    @Test
    @DisplayName("Debe lanzar GSNotFoundException si no existe")
    void findById_NotFound() {

        when(compraVentaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(GSNotFoundException.class, () -> compraVentaService.findById(999L));
    }

    @Test
    @DisplayName("Debe devolver las compras de un usuario específico")
    void findByComprador_Success() {

        when(compraVentaRepository.findByCompradorId(1L)).thenReturn(List.of(compraVenta));

        List<CompraVentaResponseDTO> result = compraVentaService.findByComprador(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(compraVentaRepository).findByCompradorId(1L);
    }

    @Test
    @DisplayName("Debe devolver las ventas de un usuario específico")
    void findByVendedor_Success() {

        when(compraVentaRepository.findByPostVentaVendedorId(2L)).thenReturn(List.of(compraVenta));

        List<CompraVentaResponseDTO> result = compraVentaService.findByVendedor(2L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(compraVentaRepository).findByPostVentaVendedorId(2L);
    }
}
