package com.tfg.angel.gameswap.backend.business.controller;

import com.tfg.angel.gameswap.backend.business.dto.request.PostVentaRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.PostVentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts-venta")
@RequiredArgsConstructor
public class PostVentaController {

    private final PostVentaService postVentaService;

    @PostMapping
    public PostVentaResponseDTO create(@RequestBody PostVentaRequestDTO dto) {
        return postVentaService.create(dto);
    }

    @GetMapping("/{id}")
    public PostVentaResponseDTO findById(@PathVariable Long id) {
        return postVentaService.findById(id);
    }

    @GetMapping
    public List<PostVentaResponseDTO> findAll() {
        return postVentaService.findAll();
    }

    @GetMapping("/vendedor/{id}")
    public List<PostVentaResponseDTO> findBySeller(@PathVariable Long id) {
        return postVentaService.findBySeller(id);
    }

    @GetMapping("/producto/{id}")
    public List<PostVentaResponseDTO> findByProduct(@PathVariable Long id) {
        return postVentaService.findByProduct(id);
    }

    @PutMapping("/{id}")
    public PostVentaResponseDTO update(@PathVariable Long id,
                                           @RequestBody PostVentaRequestDTO dto) {
        return postVentaService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        postVentaService.delete(id);
    }
}
