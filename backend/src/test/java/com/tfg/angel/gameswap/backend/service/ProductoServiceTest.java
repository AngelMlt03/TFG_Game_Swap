package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.repository.ProductoRepository;
import com.tfg.angel.gameswap.backend.business.service.impl.ProductoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @InjectMocks
    private ProductoServiceImpl service;

    @Mock
    private ProductoRepository repository;

    @Test
    void findAll_ok() {
        service.findAll();
        verify(repository).findAll();
    }
}
