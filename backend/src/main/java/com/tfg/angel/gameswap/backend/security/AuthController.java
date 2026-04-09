package com.tfg.angel.gameswap.backend.security;

import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {

        var usuario = usuarioRepository.findByNombreUsuario(request.username())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new GSBadRequestException("Constraseña incorrecta");
        }

        String accessToken = jwtService.generateToken(
                usuario.getNombreUsuario(),
                usuario.getRol().name()
        );

        String refreshToken = jwtService.generateRefreshToken(usuario.getNombreUsuario());

        return new AuthResponse(accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    public String refresh(@RequestParam String refreshToken) {

        if (!jwtService.isValid(refreshToken)) {
            throw new GSBadRequestException("Refresh token inválido");
        }

        String username = jwtService.extractUsername(refreshToken);

        var usuario = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow();

        return jwtService.generateToken(
                usuario.getNombreUsuario(),
                usuario.getRol().name()
        );
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            tokenBlacklistService.blacklist(token);
        }
    }

    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminOnly() {
        return "ok";
    }
}
