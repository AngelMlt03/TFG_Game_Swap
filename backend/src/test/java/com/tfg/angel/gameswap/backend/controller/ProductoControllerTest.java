package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.controller.ProductoController;
import com.tfg.angel.gameswap.backend.business.dto.request.ProductoRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.ProductoResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import com.tfg.angel.gameswap.backend.business.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController controller;

    private ProductoResponseDTO crearProducto() {
        return ProductoResponseDTO.builder()
                .id(1L)
                .idAPI(12345)
                .nombre("Pokemon Escarlata")
                .estado(EstadoProducto.NUEVO)
                .build();
    }

    @Test
    void create_debeCrearProducto() {

        ProductoRequestDTO dto = new ProductoRequestDTO();

        ProductoResponseDTO esperado = crearProducto();

        when(productoService.create(dto)).thenReturn(esperado);

        ProductoResponseDTO resultado = controller.create(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Pokemon Escarlata", resultado.getNombre());

        verify(productoService).create(dto);
    }

    @Test
    void findById_debeDevolverProducto() {

        Long id = 1L;

        ProductoResponseDTO esperado = crearProducto();

        when(productoService.findById(id)).thenReturn(esperado);

        ProductoResponseDTO resultado = controller.findById(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());

        verify(productoService).findById(id);
    }

    @Test
    void findAll_debeDevolverLista() {

        List<ProductoResponseDTO> esperado =
                List.of(crearProducto());

        when(productoService.findAll()).thenReturn(esperado);

        List<ProductoResponseDTO> resultado = controller.findAll();

        assertEquals(1, resultado.size());
        assertEquals("Pokemon Escarlata",
                resultado.get(0).getNombre());

        verify(productoService).findAll();
    }

    @Test
    void findByName_debeBuscarPorNombre() {

        String nombre = "Pokemon";

        List<ProductoResponseDTO> esperado =
                List.of(crearProducto());

        when(productoService.findByName(nombre))
                .thenReturn(esperado);

        List<ProductoResponseDTO> resultado =
                controller.findByName(nombre);

        assertEquals(1, resultado.size());

        verify(productoService).findByName(nombre);
    }

    @Test
    void findByState_debeBuscarPorEstado() {

        String estado = "NUEVO";

        List<ProductoResponseDTO> esperado =
                List.of(crearProducto());

        when(productoService.findByState(estado))
                .thenReturn(esperado);

        List<ProductoResponseDTO> resultado =
                controller.findByState(estado);

        assertEquals(1, resultado.size());

        verify(productoService).findByState(estado);
    }

    @Test
    void update_debeActualizarProducto() {

        Long id = 1L;

        ProductoRequestDTO dto = new ProductoRequestDTO();

        ProductoResponseDTO esperado = crearProducto();

        when(productoService.update(id, dto))
                .thenReturn(esperado);

        ProductoResponseDTO resultado =
                controller.update(id, dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(productoService).update(id, dto);
    }

    @Test
    void delete_debeEliminarProducto() {

        Long id = 1L;

        controller.delete(id);

        verify(productoService).delete(id);
    }
}
