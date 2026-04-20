package com.tfg.angel.gameswap.backend.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.Rol;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.security.AuthController;
import com.tfg.angel.gameswap.backend.security.JwtService;
import com.tfg.angel.gameswap.backend.security.TokenBlacklistService;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
import com.tfg.angel.gameswap.backend.security.records.AuthRequest;
import com.tfg.angel.gameswap.backend.security.records.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioDetailsService usuarioDetailsService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private TokenBlacklistService tokenBlacklistService;

    private Usuario usuarioPrueba;

    @BeforeEach
    void setUp() {
        usuarioPrueba = Usuario.builder()
                .nombreUsuario("angel")
                .password("encodedPassword")
                .rol(Rol.CLIENTE)
                .build();
    }

    @Test
    void login_Success() throws Exception {
        AuthRequest request = new AuthRequest("angel", "password123");

        when(usuarioRepository.findByNombreUsuario("angel")).thenReturn(Optional.of(usuarioPrueba));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyString())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refresh-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    void login_UserNotFound_ThrowsException() throws Exception {
        AuthRequest request = new AuthRequest("desconocido", "password");

        when(usuarioRepository.findByNombreUsuario("desconocido")).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void login_WrongPassword_ThrowsBadRequest() throws Exception {
        AuthRequest request = new AuthRequest("angel", "wrong");

        when(usuarioRepository.findByNombreUsuario("angel")).thenReturn(Optional.of(usuarioPrueba));
        when(passwordEncoder.matches("wrong", "encodedPassword")).thenReturn(false);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_Success() throws Exception {
        RegisterRequest request = new RegisterRequest("Angel", "angel", "angel@test.com", "pass");

        when(usuarioRepository.existsByNombreUsuario("angel")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(jwtService.generateToken(anyString(), anyString())).thenReturn("token");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void register_UserAlreadyExists_ThrowsBadRequest() throws Exception {
        RegisterRequest request = new RegisterRequest("Angel", "angel", "a@a.com", "pass");

        when(usuarioRepository.existsByNombreUsuario("angel")).thenReturn(true);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void refresh_Success() throws Exception {
        when(jwtService.isValid(anyString())).thenReturn(true);
        when(jwtService.extractUsername(anyString())).thenReturn("angel");
        when(usuarioRepository.findByNombreUsuario("angel")).thenReturn(Optional.of(usuarioPrueba));
        when(jwtService.generateToken(anyString(), anyString())).thenReturn("new-token");

        mockMvc.perform(post("/auth/refresh")
                        .param("refreshToken", "valid-refresh-token"))
                .andExpect(status().isOk())
                .andExpect(content().string("new-token"));
    }

    @Test
    void refresh_InvalidToken_ThrowsBadRequest() throws Exception {
        when(jwtService.isValid(anyString())).thenReturn(false);

        mockMvc.perform(post("/auth/refresh")
                        .param("refreshToken", "invalid-token"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void logout_WithValidHeader_BlacklistsToken() throws Exception {
        mockMvc.perform(post("/auth/logout")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk());

        verify(tokenBlacklistService).blacklist("valid-token");
    }

    @Test
    void logout_WithoutHeader_DoesNothing() throws Exception {
        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isOk());

        verify(tokenBlacklistService, never()).blacklist(anyString());
    }

    @Test
    void adminOnly_ReturnsOk() throws Exception {
        mockMvc.perform(get("/auth/admin/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }
}