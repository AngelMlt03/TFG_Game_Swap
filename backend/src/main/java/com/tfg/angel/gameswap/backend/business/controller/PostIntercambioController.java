package com.tfg.angel.gameswap.backend.business.controller;

import com.tfg.angel.gameswap.backend.business.dto.request.PostIntercambioRequestDTO;
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
        return postIntercambioService.crearPost(dto);
    }

    @GetMapping("/{id}")
    public PostIntercambioResponseDTO findById(@PathVariable Long id) {
        return postIntercambioService.obtenerPorId(id);
    }

    @GetMapping
    public List<PostIntercambioResponseDTO> findAll() {
        return postIntercambioService.obtenerTodos();
    }

    @GetMapping("/usuario/{id}")
    public List<PostIntercambioResponseDTO> findByUser(@PathVariable Long id) {
        return postIntercambioService.obtenerPorUsuario(id);
    }

    @PutMapping("/{id}")
    public PostIntercambioResponseDTO update(@PathVariable Long id,
                                                 @RequestBody PostIntercambioRequestDTO dto) {
        return postIntercambioService.actualizarPost(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        postIntercambioService.eliminarPost(id);
    }
}
