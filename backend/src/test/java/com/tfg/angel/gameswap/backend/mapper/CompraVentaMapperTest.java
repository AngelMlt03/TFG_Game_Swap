package com.tfg.angel.gameswap.backend.mapper;

import com.tfg.angel.gameswap.backend.business.mapper.CompraVentaMapper;
import com.tfg.angel.gameswap.backend.business.model.CompraVenta;
import com.tfg.angel.gameswap.backend.business.model.PostVenta;
import com.tfg.angel.gameswap.backend.business.model.Producto;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CompraVentaMapperTest {

    @Test
    void toDTO_ok() {

        Usuario usuario = Usuario.builder().id(1L).build();

        Producto producto = Producto.builder()
                .id(1L)
                .idAPI(1)
                .nombre("Juego")
                .estado(EstadoProducto.NUEVO)
                .build();

        PostVenta post = PostVenta.builder()
                .id(1L)
                .vendedor(usuario)
                .precio(10.0)
                .producto(producto)
                .estado(EstadoPost.ACTIVO)
                .build();

        CompraVenta cv = CompraVenta.builder()
                .id(1L)
                .postVenta(post)
                .comprador(usuario)
                .precio(10.0)
                .fecha(LocalDate.now())
                .build();

        var dto = CompraVentaMapper.toDTO(cv);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        //assertEquals(post.getId(), dto.getIdPostVenta());
        assertEquals(usuario.getId(), dto.getIdComprador());
        assertEquals(10.0, dto.getPrecio());
    }
}
