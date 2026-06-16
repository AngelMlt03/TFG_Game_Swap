package com.tfg.angel.gameswap.backend.controller;

import com.tfg.angel.gameswap.backend.business.controller.CompraVentaController;
import com.tfg.angel.gameswap.backend.business.dto.response.CompraVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.CompraVentaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompraVentaControllerTest {

    @Mock
    private CompraVentaService service;

    @InjectMocks
    private CompraVentaController controller;

    private CompraVentaResponseDTO crearCompraVenta() {
        return CompraVentaResponseDTO.builder()
                .id(1L)
                .idComprador(10L)
                .nombreComprador("comprador")
                .idVendedor(20L)
                .nombreVendedor("vendedor")
                .idProducto(30L)
                .idApiProducto(100L)
                .nombreProducto("Pokemon")
                .plataformaProducto("Switch")
                .estadoProducto("NUEVO")
                .precio(49.99)
                .descripcion("Descripcion")
                .fecha(LocalDate.now())
                .build();
    }

    @Test
    void create_debeCrearCompraVenta() {

        Long postId = 5L;

        CompraVentaResponseDTO esperado = crearCompraVenta();

        when(service.create(postId, 1L)).thenReturn(esperado);

        CompraVentaResponseDTO resultado = controller.create(postId);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Pokemon", resultado.getNombreProducto());

        verify(service).create(postId, 1L);
    }

    @Test
    void findById_debeDevolverCompraVenta() {

        Long id = 1L;

        CompraVentaResponseDTO esperado = crearCompraVenta();

        when(service.findById(id)).thenReturn(esperado);

        CompraVentaResponseDTO resultado = controller.findById(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());

        verify(service).findById(id);
    }

    @Test
    void findAll_debeDevolverLista() {

        List<CompraVentaResponseDTO> esperado =
                List.of(crearCompraVenta());

        when(service.findAll()).thenReturn(esperado);

        List<CompraVentaResponseDTO> resultado = controller.findAll();

        assertEquals(1, resultado.size());
        assertEquals("Pokemon", resultado.getFirst().getNombreProducto());

        verify(service).findAll();
    }

    @Test
    void findByComprador_debeDevolverLista() {

        Long compradorId = 10L;

        List<CompraVentaResponseDTO> esperado =
                List.of(crearCompraVenta());

        when(service.findByComprador(compradorId)).thenReturn(esperado);

        List<CompraVentaResponseDTO> resultado =
                controller.findByComprador(compradorId);

        assertEquals(1, resultado.size());

        verify(service).findByComprador(compradorId);
    }

    @Test
    void findByVendedor_debeDevolverLista() {

        Long vendedorId = 20L;

        List<CompraVentaResponseDTO> esperado =
                List.of(crearCompraVenta());

        when(service.findByVendedor(vendedorId)).thenReturn(esperado);

        List<CompraVentaResponseDTO> resultado =
                controller.findByVendedor(vendedorId);

        assertEquals(1, resultado.size());

        verify(service).findByVendedor(vendedorId);
    }

    @Test
    void delete_debeLlamarAlServicio() {

        Long id = 1L;

        controller.delete(id);

        verify(service).delete(id);
    }
}
