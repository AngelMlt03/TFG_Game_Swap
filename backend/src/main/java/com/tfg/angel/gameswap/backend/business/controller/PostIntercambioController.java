package com.tfg.angel.gameswap.backend.business.controller;

import com.tfg.angel.gameswap.backend.business.dto.request.PostIntercambioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.request.PostVentaRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostIntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.PostIntercambioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts-intercambio")
@RequiredArgsConstructor
public class PostIntercambioController {

    private final PostIntercambioService postIntercambioService;

    @PostMapping
    public PostIntercambioResponseDTO create(@RequestBody PostIntercambioRequestDTO dto) {
        return postIntercambioService.create(dto);
    }

    @GetMapping("/{id}")
    public PostIntercambioResponseDTO findById(@PathVariable Long id) {
        return postIntercambioService.findById(id);
    }

    @GetMapping
    public List<PostIntercambioResponseDTO> findAll() {
        return postIntercambioService.findAll();
    }

    @GetMapping("/usuario/{id}")
    public List<PostIntercambioResponseDTO> findByUser(@PathVariable Long id) {
        return postIntercambioService.findByUser(id);
    }

    @PutMapping("/{id}")
    public PostIntercambioResponseDTO update(@PathVariable Long id, @RequestBody PostIntercambioRequestDTO dto) {
        return postIntercambioService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        postIntercambioService.delete(id);
    }

    @PostMapping("/{id}/convertir")
    public void convertirIntercambioAVenta(@PathVariable Long id, @RequestBody PostVentaRequestDTO dto) {
        postIntercambioService.convertirIntercambioAVenta(id, dto);
    }

    @GetMapping("/intercambio-sugerido")
    public boolean existeIntercambioSugerido(@RequestParam String tuJuego, @RequestParam String juegoBuscado) {
        return postIntercambioService.existeIntercambioInverso(tuJuego, juegoBuscado);
    }
}
