package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.controller.PostVentaController;
import com.tfg.angel.gameswap.backend.business.dto.request.PostVentaRequestDTO;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import com.tfg.angel.gameswap.backend.business.service.PostVentaService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class PostVentaControllerTest {

    @Test
    void testAllMethods() {

        PostVentaService service = mock(PostVentaService.class);
        PostVentaController controller = new PostVentaController(service);

        PostVentaRequestDTO postVentaRequestDTO = new PostVentaRequestDTO();

        controller.findAll();
        controller.findById(1L);
        controller.findByProduct(1L);
        controller.findActive();
        controller.create(postVentaRequestDTO);
        controller.update(1L, postVentaRequestDTO);
        controller.delete(1L);

        verify(service).findAll();
        verify(service).findById(1L);
        verify(service).findByProduct(1L);
        verify(service).findByEstado(EstadoPost.ACTIVO);
        verify(service).create(postVentaRequestDTO);
        verify(service).update(1L, postVentaRequestDTO);
        verify(service).delete(1L);
    }
}
