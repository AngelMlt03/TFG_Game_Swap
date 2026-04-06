package com.tfg.angel.gameswap.backend.business.controller;

import com.tfg.angel.gameswap.backend.business.dto.request.CompraVentaRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.CompraVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.CompraVentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compra-venta")
@RequiredArgsConstructor
public class CompraVentaController {

    private final CompraVentaService service;

    @PostMapping
    public CompraVentaResponseDTO create(@RequestBody CompraVentaRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public CompraVentaResponseDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    public List<CompraVentaResponseDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/comprador/{id}")
    public List<CompraVentaResponseDTO> findByBuyer(@PathVariable Long id) {
        return service.findByComprador(id);
    }

    @GetMapping("/vendedor/{id}")
    public List<CompraVentaResponseDTO> findBySeller(@PathVariable Long id) {
        return service.findByVendedor(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
