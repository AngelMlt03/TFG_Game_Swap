package com.tfg.angel.gameswap.backend.mapper;

import com.tfg.angel.gameswap.backend.business.mapper.ProductoCarritoMapper;
import com.tfg.angel.gameswap.backend.business.model.*;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductoCarritoMapperTest {

    @Test
    void toDTO_ok() {

        Usuario usuario = Usuario.builder().id(1L).build();

        Producto producto = Producto.builder()
                .id(1L)
                .idAPI(1)
                .nombre("PS5")
                .estado(EstadoProducto.NUEVO)
                .build();

        PostVenta postVenta = PostVenta.builder()
                .id(1L)
                .producto(producto)
                .vendedor(usuario)
                .build();

        Carrito carrito = Carrito.builder()
                .id(1L)
                .usuario(usuario)
                .coste(50.0)
                .productos(List.of())
                .build();

        ProductoCarrito pc = ProductoCarrito.builder()
                .id(1L)
                .postVenta(postVenta)
                .carrito(carrito)
                .build();

        var dto = ProductoCarritoMapper.toDTO(pc);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(postVenta.getId(), dto.getIdPostVenta());
    }
}
