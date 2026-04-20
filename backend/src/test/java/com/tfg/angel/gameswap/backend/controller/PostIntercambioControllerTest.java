package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.controller.PostIntercambioController;
import com.tfg.angel.gameswap.backend.business.dto.request.PostIntercambioRequestDTO;
import com.tfg.angel.gameswap.backend.business.service.PostIntercambioService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class PostIntercambioControllerTest {

    @Test
    void testAllMethods() {

        PostIntercambioService service = mock(PostIntercambioService.class);
        PostIntercambioController controller = new PostIntercambioController(service);

        PostIntercambioRequestDTO postIntercambioRequestDTO = new PostIntercambioRequestDTO();

        controller.findAll();
        controller.findById(1L);
        controller.create(postIntercambioRequestDTO);
        controller.update(1L, postIntercambioRequestDTO);
        controller.delete(1L);

        verify(service).findAll();
        verify(service).findById(1L);
        verify(service).create(postIntercambioRequestDTO);
        verify(service).update(1L, postIntercambioRequestDTO);
        verify(service).delete(1L);
    }
}
