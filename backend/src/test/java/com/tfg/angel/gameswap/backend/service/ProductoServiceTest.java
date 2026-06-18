package com.tfg.angel.gameswap.backend.service;

import com.tfg.angel.gameswap.backend.business.dto.request.ProductoRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.ProductoResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.Producto;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import com.tfg.angel.gameswap.backend.business.repository.ProductoRepository;
import com.tfg.angel.gameswap.backend.business.service.impl.ProductoServiceImpl;
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
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;
    private ProductoRequestDTO productoDTO;

    @BeforeEach
    void setUp() {
        producto = Producto.builder()
                .id(1L)
                .idAPI(123)
                .nombre("Super Mario Odyssey")
                .estado(EstadoProducto.NUEVO)
                .build();

        productoDTO = ProductoRequestDTO.builder()
                .idAPI(123)
                .nombre("Super Mario Odyssey")
                .estado(EstadoProducto.NUEVO)
                .build();
    }

    @Test
    @DisplayName("Debe crear un producto correctamente")
    void create_Success() {

        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        ProductoResponseDTO response = productoService.create(productoDTO);

        assertNotNull(response);
        assertEquals("Super Mario Odyssey", response.getNombre());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    @DisplayName("Debe retornar una lista filtrada por estado válido")
    void findByState_Success() {

        when(productoRepository.findByEstado(EstadoProducto.NUEVO)).thenReturn(List.of(producto));

        List<ProductoResponseDTO> result = productoService.findByState("nuevo");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(productoRepository).findByEstado(EstadoProducto.NUEVO);
    }

    @Test
    @DisplayName("Debe lanzar GSBadRequestException si el estado no es válido")
    void findByState_ThrowsBadRequest_WhenInvalidState() {

        assertThrows(GSBadRequestException.class, () ->
                productoService.findByState("ESTADO_INVENTADO")
        );
        verify(productoRepository, never()).findByEstado(any());
    }

    @Test
    @DisplayName("Debe actualizar un producto correctamente")
    void update_Success() {

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        ProductoResponseDTO response = productoService.update(1L, productoDTO);

        assertNotNull(response);
        verify(productoRepository).save(producto);
    }

    @Test
    @DisplayName("Debe lanzar GSNotFoundException al intentar actualizar un producto inexistente")
    void update_ThrowsNotFound() {

        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(GSNotFoundException.class, () -> productoService.update(99L, productoDTO));
        verify(productoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe eliminar un producto si existe")
    void delete_Success() {

        when(productoRepository.existsById(1L)).thenReturn(true);

        productoService.delete(1L);

        verify(productoRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Debe lanzar GSNotFoundException al intentar eliminar un producto que no existe")
    void delete_ThrowsNotFound() {

        when(productoRepository.existsById(1L)).thenReturn(false);

        assertThrows(GSNotFoundException.class, () -> productoService.delete(1L));
        verify(productoRepository, never()).deleteById(anyLong());
    }

    private Producto producto() {
        return Producto.builder()
                .id(1L)
                .idAPI(100)
                .nombre("Zelda")
                .estado(EstadoProducto.NUEVO)
                .build();
    }

    private ProductoRequestDTO dto() {
        ProductoRequestDTO dto = new ProductoRequestDTO();

        dto.setIdAPI(100);
        dto.setNombre("Zelda");
        dto.setEstado(EstadoProducto.NUEVO);

        return dto;
    }

    @Test
    void create_ok() {

        when(productoRepository.save(any(Producto.class)))
                .thenAnswer(i -> i.getArgument(0));

        ProductoResponseDTO resultado =
                productoService.create(dto());

        assertNotNull(resultado);

        verify(productoRepository)
                .save(any(Producto.class));
    }

    @Test
    void findById_ok() {

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(producto()));

        ProductoResponseDTO resultado =
                productoService.findById(1L);

        assertNotNull(resultado);
        assertEquals("Zelda", resultado.getNombre());
    }

    @Test
    void findById_notFound() {

        when(productoRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                GSNotFoundException.class,
                () -> productoService.findById(1L)
        );
    }

    @Test
    void findAll_ok() {

        when(productoRepository.findAll())
                .thenReturn(List.of(producto()));

        List<ProductoResponseDTO> resultado =
                productoService.findAll();

        assertEquals(1, resultado.size());
    }

    @Test
    void findByName_ok() {

        when(productoRepository.findByNombreContainingIgnoreCase("zel"))
                .thenReturn(List.of(producto()));

        List<ProductoResponseDTO> resultado =
                productoService.findByName("zel");

        assertEquals(1, resultado.size());
    }

    @Test
    void findByState_ok() {

        when(productoRepository.findByEstado(EstadoProducto.NUEVO))
                .thenReturn(List.of(producto()));

        List<ProductoResponseDTO> resultado =
                productoService.findByState("nuevo");

        assertEquals(1, resultado.size());
    }

    @Test
    void findByState_invalidState() {

        assertThrows(
                GSBadRequestException.class,
                () -> productoService.findByState("ESTADO_INVENTADO")
        );
    }

    @Test
    void update_ok() {

        Producto productou = producto();

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(productou));

        when(productoRepository.save(any(Producto.class)))
                .thenAnswer(i -> i.getArgument(0));

        ProductoResponseDTO resultado =
                productoService.update(1L, dto());

        assertNotNull(resultado);

        verify(productoRepository)
                .save(productou);
    }

    @Test
    void delete_notFound() {

        when(productoRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(
                GSNotFoundException.class,
                () -> productoService.delete(1L)
        );
    }
}
