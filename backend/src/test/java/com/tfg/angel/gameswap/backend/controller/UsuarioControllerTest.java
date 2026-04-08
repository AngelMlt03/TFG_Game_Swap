package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.controller.UsuarioController;
import com.tfg.angel.gameswap.backend.business.dto.request.UsuarioRequestDTO;
import com.tfg.angel.gameswap.backend.business.service.UsuarioService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @Test
    void testAllMethods() {

        UsuarioService service = mock(UsuarioService.class);
        UsuarioController controller = new UsuarioController(service);

        UsuarioRequestDTO usuarioRequestDTO = new UsuarioRequestDTO();

        controller.findAll();
        controller.findById(1L);
        controller.create(usuarioRequestDTO);
        controller.update(1L, usuarioRequestDTO);
        controller.delete(1L);

        verify(service).findAll();
        verify(service).findById(1L);
        verify(service).create(usuarioRequestDTO);
        verify(service).update(1L, usuarioRequestDTO);
        verify(service).delete(1L);
    }
}
