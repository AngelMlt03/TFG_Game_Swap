package com.tfg.angel.gameswap.backend.auth;

import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.Rol;
import com.tfg.angel.gameswap.backend.security.UsuarioDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDetailsTest {

    private UsuarioDetails usuarioDetails;

    @BeforeEach
    void setUp() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nombreUsuario("angel_test")
                .password("password123")
                .rol(Rol.ADMIN)
                .build();

        usuarioDetails = new UsuarioDetails(usuario);
    }

    @Test
    void getAuthorities_ShouldReturnRoleWithPrefix() {

        var authorities = usuarioDetails.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void getPassword_ShouldReturnUserPassword() {
        assertEquals("password123", usuarioDetails.getPassword());
    }

    @Test
    void getUsername_ShouldReturnNombreUsuario() {
        assertEquals("angel_test", usuarioDetails.getUsername());
    }

    @Test
    void getId_ShouldReturnUserId() {
        assertEquals(1L, usuarioDetails.getId());
    }

    @Test
    void booleanMethods_ShouldReturnTrue() {
        assertTrue(usuarioDetails.isAccountNonExpired());
        assertTrue(usuarioDetails.isAccountNonLocked());
        assertTrue(usuarioDetails.isCredentialsNonExpired());
        assertTrue(usuarioDetails.isEnabled());
    }
}
