package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.model.*;
import com.tfg.angel.gameswap.backend.business.repository.*;
import com.tfg.angel.gameswap.backend.business.service.impl.CompraVentaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CompraVentaServiceTest {

    @InjectMocks
    private CompraVentaServiceImpl service;

    @Mock
    private CompraVentaRepository compraVentaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PostVentaRepository postVentaRepository;

    @Test
    void create_ok() {

        Usuario comprador = Usuario.builder().id(1L).saldo(100.0).build();
        Usuario vendedor = Usuario.builder().id(2L).saldo(50.0).build();

        Producto producto = Producto.builder().id(1L).nombre("Juego").build();

        PostVenta post = PostVenta.builder()
                .id(1L)
                .precio(30.0)
                .producto(producto)
                .vendedor(vendedor)
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(comprador));
        when(postVentaRepository.findById(1L)).thenReturn(Optional.of(post));
        when(compraVentaRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        var result = service.create(1L, 1L);

        assertNotNull(result);
        assertEquals(70.0, comprador.getSaldo());
        assertEquals(80.0, vendedor.getSaldo());
    }

    @Test
    void create_insufficient_balance() {

        Usuario comprador = Usuario.builder().id(1L).saldo(10.0).build();
        Usuario vendedor = Usuario.builder().id(2L).saldo(50.0).build();

        PostVenta post = PostVenta.builder()
                .id(1L)
                .precio(30.0)
                .vendedor(vendedor)
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(comprador));
        when(postVentaRepository.findById(1L)).thenReturn(Optional.of(post));

        assertThrows(RuntimeException.class, () -> service.create(1L, 1L));
    }
}
