package com.tfg.angel.gameswap.backend.mapper;

import com.tfg.angel.gameswap.backend.business.mapper.ProductoMapper;
import com.tfg.angel.gameswap.backend.business.model.Producto;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductoMapperTest {

    @Test
    void toDTO_ok() {
        Producto producto = Producto.builder()
                .id(1L)
                .idAPI(1)
                .nombre("PS5")
                .estado(EstadoProducto.NUEVO)
                .build();

        var dto = ProductoMapper.toDTO(producto);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(1, dto.getIdAPI());
        assertEquals("PS5", dto.getNombre());
        assertEquals(EstadoProducto.NUEVO, dto.getEstado());
    }
}