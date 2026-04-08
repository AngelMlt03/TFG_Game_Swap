package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.repository.PostVentaRepository;
import com.tfg.angel.gameswap.backend.business.service.impl.PostVentaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostVentaServiceTest {

    @InjectMocks
    private PostVentaServiceImpl service;

    @Mock
    private PostVentaRepository repository;

    @Test
    void findAll_ok() {

        when(repository.findAll()).thenReturn(List.of());

        service.findAll();

        verify(repository).findAll();
    }
}
