package com.tfg.angel.gameswap.backend.business.controller;

import com.tfg.angel.gameswap.backend.business.dto.request.ChangePasswordRequest;
import com.tfg.angel.gameswap.backend.business.dto.request.UsuarioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PerfilPublicoDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostBusquedaDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.UsuarioResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.UsuarioService;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioDetailsService usuarioDetailsService;

    @PostMapping
    public UsuarioResponseDTO create(@RequestBody UsuarioRequestDTO dto) {
        return usuarioService.create(dto);
    }

    @GetMapping
    public List<UsuarioResponseDTO> findAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO findById(@PathVariable Long id) {
        return usuarioService.findById(id);
    }

    @PutMapping("/{id}")
    public UsuarioResponseDTO update(@PathVariable Long id, @RequestBody UsuarioRequestDTO dto) {
        return usuarioService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        usuarioService.delete(id);
    }

    @GetMapping("/username/{username}")
    public UsuarioResponseDTO findByUsername(@PathVariable String username) {
        return usuarioService.findByUsername(username);
    }

    @PostMapping("/change-password")
    public void changePassword(@RequestBody ChangePasswordRequest request, Authentication auth) {
        usuarioService.changePassword(auth.getName(), request);
    }

    @GetMapping("/saldo")
    public Double getSaldo() {
        return usuarioDetailsService.obtenerUsuarioActual().getSaldo();
    }

    @PostMapping("/saldo")
    public ResponseEntity<Double> sumarSaldo(@RequestParam Double cantidad) {
        return usuarioService.addSaldo(cantidad);
    }

    @GetMapping("/perfil/{nombreUsuario}")
    public PerfilPublicoDTO getPerfilPublico(@PathVariable String nombreUsuario) {
        return usuarioService.getPerfilPublico(nombreUsuario);
    }

    @GetMapping("/{nombreUsuario}/ventas")
    public List<PostBusquedaDTO> misVentas(@PathVariable String nombreUsuario) {
        return usuarioService.findVentasByUsuario(nombreUsuario);
    }

    @GetMapping("/{nombreUsuario}/intercambios")
    public List<PostBusquedaDTO> misIntercambios(@PathVariable String nombreUsuario) {
        return usuarioService.findIntercambiosByUsuario(nombreUsuario);
    }
}
