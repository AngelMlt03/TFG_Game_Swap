package com.tfg.angel.gameswap.backend.business.controller;

import com.tfg.angel.gameswap.backend.business.dto.request.UsuarioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.UsuarioResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public UsuarioResponseDTO create(@RequestBody UsuarioRequestDTO dto) {
        return usuarioService.crearUsuario(dto);
    }

    @GetMapping
    public List<UsuarioResponseDTO> findAll() {
        return usuarioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO findById(@PathVariable Long id) {
        return usuarioService.obtenerUsuarioPorId(id);
    }

    @PutMapping("/{id}")
    public UsuarioResponseDTO update(@PathVariable Long id, @RequestBody UsuarioRequestDTO dto) {
        return usuarioService.actualizarUsuario(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
    }
}
