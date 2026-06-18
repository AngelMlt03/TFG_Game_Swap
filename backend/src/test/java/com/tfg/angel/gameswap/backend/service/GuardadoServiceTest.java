package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.request.GuardadoRequestDTO;
import com.tfg.angel.gameswap.backend.business.model.Guardado;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
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
}
