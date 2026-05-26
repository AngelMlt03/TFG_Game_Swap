package com.tfg.angel.gameswap.backend.business.controller;

import com.tfg.angel.gameswap.backend.business.dto.request.ReviewRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.CompraVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.IntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.ReviewResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.ReviewService;
import com.tfg.angel.gameswap.backend.business.service.impl.HistorialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;
    private final HistorialService historialService;

    @PostMapping
    public ReviewResponseDTO create(@RequestBody ReviewRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public ReviewResponseDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    public List<ReviewResponseDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/reviewed/{id}")
    public List<ReviewResponseDTO> findByReviewed(@PathVariable Long id) {
        return service.findByReviewed(id);
    }

    @GetMapping("/reviewer/{id}")
    public List<ReviewResponseDTO> findByReviewer(@PathVariable Long id) {
        return service.findByReviewer(id);
    }

    @GetMapping("/mis-reviews")
    public List<ReviewResponseDTO> getMisReviews() {
        return service.getMisReviews();
    }

    @GetMapping("/enviadas")
    public List<ReviewResponseDTO> getReviewsEnviadas() {
        return service.getReviewsEnviadas();
    }

    @GetMapping("/compra/{id}")
    public CompraVentaResponseDTO getCompra(@PathVariable Long id) {
        return historialService.getCompra(id);
    }

    @GetMapping("/intercambio/{id}")
    public IntercambioResponseDTO getIntercambio(@PathVariable Long id) {
        return historialService.getIntercambio(id);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<ReviewResponseDTO> getByUsuario(@PathVariable Long idUsuario) {
        return service.getByUsuario(idUsuario);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
