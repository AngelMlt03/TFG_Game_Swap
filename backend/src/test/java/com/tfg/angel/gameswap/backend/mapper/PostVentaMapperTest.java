package com.tfg.angel.gameswap.backend.mapper;

import com.tfg.angel.gameswap.backend.business.mapper.PostVentaMapper;
import com.tfg.angel.gameswap.backend.business.model.PostVenta;
import com.tfg.angel.gameswap.backend.business.model.Producto;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostVentaMapperTest {

    @Test
    void toDTO_ok() {

        Usuario usuario = Usuario.builder().id(1L).build();

        Producto producto = Producto.builder()
                .id(1L)
                .idAPI(1)
                .nombre("PS5")
                .estado(EstadoProducto.NUEVO)
                .build();

        PostVenta post = PostVenta.builder()
                .id(1L)
                .vendedor(usuario)
                .precio(10.0)
                .producto(producto)
                .estado(EstadoPost.ACTIVO)
                .build();

        var dto = PostVentaMapper.toDTO(post);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(usuario.getId(), dto.getIdVendedor());
        assertEquals(10.0, dto.getPrecio());
        assertEquals(producto.getId(), dto.getIdProducto());
    }
}
