package com.tfg.angel.gameswap.backend.business.controller;

import com.tfg.angel.gameswap.backend.business.dto.request.ReviewRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.ReviewResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;

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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
