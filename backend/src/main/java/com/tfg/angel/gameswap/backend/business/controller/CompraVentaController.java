package com.tfg.angel.gameswap.backend.business.controller;

import com.tfg.angel.gameswap.backend.business.dto.response.CompraVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.CompraVentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
public class CompraVentaController {

    private final CompraVentaService service;

    @PostMapping("/{idPostVenta}")
    public CompraVentaResponseDTO create(@PathVariable Long idPostVenta) {

        Long idUsuario = 1L;

        return service.create(idPostVenta, idUsuario);
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
    public List<CompraVentaResponseDTO> findByComprador(@PathVariable Long id) {
        return service.findByComprador(id);
    }

    @GetMapping("/vendedor/{id}")
    public List<CompraVentaResponseDTO> findByVendedor(@PathVariable Long id) {
        return service.findByVendedor(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
