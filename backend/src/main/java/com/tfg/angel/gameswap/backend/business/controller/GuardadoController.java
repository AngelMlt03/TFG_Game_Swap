package com.tfg.angel.gameswap.backend.business.controller;

import com.tfg.angel.gameswap.backend.business.dto.request.GuardadoRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostIntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.GuardadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guardados")
@RequiredArgsConstructor
public class GuardadoController {

    private final GuardadoService guardadoService;

    @PostMapping
    public void guardar(@RequestBody GuardadoRequestDTO dto) {
        guardadoService.guardar(dto);
    }

    @DeleteMapping("/{idPost}/{tipoPost}")
    public void eliminar(@PathVariable Long idPost, @PathVariable String tipoPost) {
        guardadoService.eliminar(idPost, tipoPost);
    }

    @GetMapping("/ventas")
    public List<PostVentaResponseDTO> getVentasGuardadas() {
        return guardadoService.getVentasGuardadas();
    }

    @GetMapping("/intercambios")
    public List<PostIntercambioResponseDTO> getIntercambiosGuardados() {
        return guardadoService.getIntercambiosGuardados();
    }

    @GetMapping("/existe/{idPost}/{tipoPost}")
    public boolean existe(@PathVariable Long idPost, @PathVariable String tipoPost) {
        return guardadoService.existe(idPost, tipoPost);
    }
}
