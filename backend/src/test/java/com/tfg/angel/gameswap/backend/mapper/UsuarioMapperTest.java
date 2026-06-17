package com.tfg.angel.gameswap.backend.mapper;

import com.tfg.angel.gameswap.backend.business.mapper.UsuarioMapper;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.Rol;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioMapperTest {

    @Test
    void toDTO_ok() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombre("Angel")
                .nombreUsuario("angel")
                .correo("a@gmail.com")
                .fechaNacimiento(LocalDate.now())
                .saldo(100.0)
                .estrellas(5.0)
                .rol(Rol.CLIENTE)
                .build();

        var dto = UsuarioMapper.toDTO(usuario);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Angel", dto.getNombre());
        assertEquals("angel", dto.getNombreUsuario());
        assertEquals("a@gmail.com", dto.getCorreo());
        assertEquals(100.0, dto.getSaldo());
        assertEquals(5.0, dto.getEstrellas());
        assertEquals(Rol.CLIENTE, dto.getRol());
    }
}
