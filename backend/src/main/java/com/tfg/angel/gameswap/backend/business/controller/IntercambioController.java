package com.tfg.angel.gameswap.backend.business.controller;

import com.tfg.angel.gameswap.backend.business.dto.response.IntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.IntercambioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/intercambios")
@RequiredArgsConstructor
public class IntercambioController {

    private final IntercambioService service;

    @PostMapping("/{idPost}")
    public IntercambioResponseDTO create(@PathVariable Long idPost) {

        Long idUsuario = 1L;
        return service.create(idPost, idUsuario);
    }

    @GetMapping("/{id}")
    public IntercambioResponseDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    public List<IntercambioResponseDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/usuario/{id}")
    public List<IntercambioResponseDTO> findByUsuario(@PathVariable Long id) {
        return service.findByUsuario(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
