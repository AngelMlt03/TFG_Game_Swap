package com.tfg.angel.gameswap.backend.mapper;

import com.tfg.angel.gameswap.backend.business.mapper.CarritoMapper;
import com.tfg.angel.gameswap.backend.business.model.Carrito;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarritoMapperTest {

    @Test
    void toDTO_ok() {

        Usuario usuario = Usuario.builder().id(1L).build();

        Carrito carrito = Carrito.builder()
                .id(1L)
                .usuario(usuario)
                .coste(50.0)
                .productos(List.of())
                .build();

        var dto = CarritoMapper.toDTO(carrito);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getIdUsuario());
    }
}