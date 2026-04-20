package com.tfg.angel.gameswap.backend.mapper;

import com.tfg.angel.gameswap.backend.business.mapper.PostIntercambioMapper;
import com.tfg.angel.gameswap.backend.business.model.PostIntercambio;
import com.tfg.angel.gameswap.backend.business.model.Producto;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostIntercambioMapperTest {

    @Test
    void toDTO_ok() {

        Usuario usuario = Usuario.builder().id(1L).build();

        Producto producto1 = Producto.builder()
                .id(1L)
                .idAPI(1)
                .nombre("Juego 1")
                .estado(EstadoProducto.NUEVO)
                .build();

        Producto producto2 = Producto.builder()
                .id(2L)
                .idAPI(2)
                .nombre("Juego 2")
                .estado(EstadoProducto.NUEVO)
                .build();

        PostIntercambio post = PostIntercambio.builder()
                .id(1L)
                .usuario(usuario)
                .producto(producto1)
                .productoCambio(producto2)
                .estado(EstadoPost.ACTIVO)
                .build();

        var dto = PostIntercambioMapper.toDTO(post);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(usuario.getId(), dto.getIdUsuario());
        assertEquals(producto1.getId(), dto.getIdProducto());
        assertEquals(producto2.getId(), dto.getIdProductoCambio());
    }
}
