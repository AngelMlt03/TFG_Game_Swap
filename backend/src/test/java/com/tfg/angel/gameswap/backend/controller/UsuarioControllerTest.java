package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.controller.UsuarioController;
import com.tfg.angel.gameswap.backend.business.dto.request.ChangePasswordRequest;
import com.tfg.angel.gameswap.backend.business.dto.request.UsuarioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PerfilPublicoDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostBusquedaDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.UsuarioResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.UsuarioService;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private UsuarioDetailsService usuarioDetailsService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UsuarioController controller;

    private UsuarioResponseDTO crearUsuario() {
        return UsuarioResponseDTO.builder()
                .id(1L)
                .nombre("Angel")
                .nombreUsuario("angel")
                .correo("angel@test.com")
                .saldo(100.0)
                .build();
    }

    @Test
    void create_debeCrearUsuario() {

        UsuarioRequestDTO dto = new UsuarioRequestDTO();

        UsuarioResponseDTO esperado = crearUsuario();

        when(usuarioService.create(dto)).thenReturn(esperado);

        UsuarioResponseDTO resultado = controller.create(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(usuarioService).create(dto);
    }

    @Test
    void findAll_debeDevolverUsuarios() {

        List<UsuarioResponseDTO> esperado =
                List.of(crearUsuario());

        when(usuarioService.findAll()).thenReturn(esperado);

        List<UsuarioResponseDTO> resultado = controller.findAll();

        assertEquals(1, resultado.size());

        verify(usuarioService).findAll();
    }

    @Test
    void findById_debeBuscarPorId() {

        Long id = 1L;

        UsuarioResponseDTO esperado = crearUsuario();

        when(usuarioService.findById(id)).thenReturn(esperado);

        UsuarioResponseDTO resultado = controller.findById(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());

        verify(usuarioService).findById(id);
    }

    @Test
    void update_debeActualizarUsuario() {

        Long id = 1L;

        UsuarioRequestDTO dto = new UsuarioRequestDTO();

        UsuarioResponseDTO esperado = crearUsuario();

        when(usuarioService.update(id, dto)).thenReturn(esperado);

        UsuarioResponseDTO resultado =
                controller.update(id, dto);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());

        verify(usuarioService).update(id, dto);
    }

    @Test
    void delete_debeEliminarUsuario() {

        Long id = 1L;

        controller.delete(id);

        verify(usuarioService).delete(id);
    }

    @Test
    void findByUsername_debeBuscarUsuario() {

        String username = "angel";

        UsuarioResponseDTO esperado = crearUsuario();

        when(usuarioService.findByUsername(username))
                .thenReturn(esperado);

        UsuarioResponseDTO resultado =
                controller.findByUsername(username);

        assertNotNull(resultado);

        verify(usuarioService).findByUsername(username);
    }

    @Test
    void changePassword_debeCambiarPassword() {

        ChangePasswordRequest request =
                new ChangePasswordRequest("","");

        when(authentication.getName())
                .thenReturn("angel");

        controller.changePassword(request, authentication);

        verify(usuarioService)
                .changePassword("angel", request);
    }

    @Test
    void sumarSaldo_debeSumarSaldo() {

        Double cantidad = 50.0;

        ResponseEntity<Double> esperado =
                ResponseEntity.ok(200.0);

        when(usuarioService.addSaldo(cantidad))
                .thenReturn(esperado);

        ResponseEntity<Double> resultado =
                controller.sumarSaldo(cantidad);

        assertEquals(200.0, resultado.getBody());

        verify(usuarioService).addSaldo(cantidad);
    }

    @Test
    void getPerfilPublico_debeDevolverPerfil() {

        String usuario = "angel";

        PerfilPublicoDTO perfil =
                PerfilPublicoDTO.builder()
                        .nombre("Angel")
                        .nombreUsuario(usuario)
                        .correo("angel@test.com")
                        .estrellas(5.0)
                        .build();

        when(usuarioService.getPerfilPublico(usuario))
                .thenReturn(perfil);

        PerfilPublicoDTO resultado =
                controller.getPerfilPublico(usuario);

        assertNotNull(resultado);
        assertEquals(usuario, resultado.getNombreUsuario());

        verify(usuarioService)
                .getPerfilPublico(usuario);
    }

    @Test
    void misVentas_debeDevolverVentas() {

        String usuario = "angel";

        List<PostBusquedaDTO> esperado =
                List.of(mock(PostBusquedaDTO.class));

        when(usuarioService.findVentasByUsuario(usuario))
                .thenReturn(esperado);

        List<PostBusquedaDTO> resultado =
                controller.misVentas(usuario);

        assertEquals(1, resultado.size());

        verify(usuarioService)
                .findVentasByUsuario(usuario);
    }

    @Test
    void misIntercambios_debeDevolverIntercambios() {

        String usuario = "angel";

        List<PostBusquedaDTO> esperado =
                List.of(mock(PostBusquedaDTO.class));

        when(usuarioService.findIntercambiosByUsuario(usuario))
                .thenReturn(esperado);

        List<PostBusquedaDTO> resultado =
                controller.misIntercambios(usuario);

        assertEquals(1, resultado.size());

        verify(usuarioService)
                .findIntercambiosByUsuario(usuario);
    }
}