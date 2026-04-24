package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.request.UsuarioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.UsuarioResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.Rol;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.impl.UsuarioServiceImpl;
import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private UsuarioRequestDTO usuarioDTO;
    private Usuario usuarioEntidad;

    @BeforeEach
    void setUp() {

        usuarioDTO = UsuarioRequestDTO.builder()
                .nombre("Angel")
                .nombreUsuario("angel_dev")
                .correo("angel@example.com")
                .fechaNacimiento(LocalDate.of(2003, 9, 2))
                .build();

        usuarioEntidad = Usuario.builder()
                .id(1L)
                .nombre("Angel")
                .nombreUsuario("angel_dev")
                .correo("angel@example.com")
                .rol(Rol.CLIENTE)
                .build();
    }

    @Test
    @DisplayName("Debe crear un usuario correctamente si el correo y el nickname son únicos")
    void create_Success() {

        when(usuarioRepository.existsByCorreo(usuarioDTO.getCorreo())).thenReturn(false);
        when(usuarioRepository.existsByNombreUsuario(usuarioDTO.getNombreUsuario())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEntidad);

        UsuarioResponseDTO response = usuarioService.create(usuarioDTO);

        assertNotNull(response);
        assertEquals("angel_dev", response.getNUsuario());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe lanzar GSBadRequestException si el correo ya existe")
    void create_ThrowsException_WhenEmailExists() {

        when(usuarioRepository.existsByCorreo(usuarioDTO.getCorreo())).thenReturn(true);

        GSBadRequestException exception = assertThrows(GSBadRequestException.class, () ->
                usuarioService.create(usuarioDTO)
        );

        assertEquals("El correo ya está en uso", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar GSBadRequestException si el nombre de usuario ya existe")
    void create_ThrowsException_WhenUsernameExists() {

        when(usuarioRepository.existsByCorreo(usuarioDTO.getCorreo())).thenReturn(false);
        when(usuarioRepository.existsByNombreUsuario(usuarioDTO.getNombreUsuario())).thenReturn(true);

        assertThrows(GSBadRequestException.class, () -> usuarioService.create(usuarioDTO));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe actualizar los datos de un usuario existente")
    void update_Success() {

        Long id = 1L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioEntidad));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEntidad);

        UsuarioResponseDTO response = usuarioService.update(id, usuarioDTO);

        assertNotNull(response);
        verify(usuarioRepository).save(usuarioEntidad);
    }

    @Test
    @DisplayName("Debe lanzar GSNotFoundException al intentar actualizar un usuario inexistente")
    void update_ThrowsNotFound() {

        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(GSNotFoundException.class, () -> usuarioService.update(99L, usuarioDTO));
    }

    @Test
    @DisplayName("Debe eliminar un usuario si existe por ID")
    void delete_Success() {

        when(usuarioRepository.existsById(1L)).thenReturn(true);

        usuarioService.delete(1L);

        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Debe lanzar GSNotFoundException al intentar eliminar un usuario que no existe")
    void delete_ThrowsNotFound() {

        when(usuarioRepository.existsById(1L)).thenReturn(false);

        assertThrows(GSNotFoundException.class, () -> usuarioService.delete(1L));
        verify(usuarioRepository, never()).deleteById(anyLong());
    }
}
