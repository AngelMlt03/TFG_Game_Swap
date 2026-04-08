package com.tfg.angel.gameswap.backend.mapper;

import com.tfg.angel.gameswap.backend.business.mapper.IntercambioMapper;
import com.tfg.angel.gameswap.backend.business.model.Intercambio;
import com.tfg.angel.gameswap.backend.business.model.PostIntercambio;
import com.tfg.angel.gameswap.backend.business.model.Producto;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class IntercambioMapperTest {

    @Test
    void toDTO_ok() {

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

        Usuario usuario = Usuario.builder().id(1L).build();

        PostIntercambio post = PostIntercambio.builder()
                .id(1L)
                .usuario(usuario)
                .producto(producto1)
                .productoCambio(producto2)
                .estado(EstadoPost.ACTIVO)
                .build();

        Intercambio intercambio = Intercambio.builder()
                .id(1L)
                .postIntercambio(post)
                .usuarioCambio(usuario)
                .fecha(LocalDate.now())
                .build();

        var dto = IntercambioMapper.toDTO(intercambio);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(post.getId(), dto.getIdPostIntercambio());
        assertEquals(usuario.getId(), dto.getIdUsuarioCambio());
    }
}
