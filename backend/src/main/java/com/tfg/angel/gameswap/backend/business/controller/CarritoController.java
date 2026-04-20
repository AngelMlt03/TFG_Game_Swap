package com.tfg.angel.gameswap.backend.business.controller;

import com.tfg.angel.gameswap.backend.business.dto.request.CarritoRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.request.ProductoCarritoRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.CarritoResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carritos")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService service;

    @PostMapping
    public CarritoResponseDTO create(@RequestBody CarritoRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping("/usuario/{id}")
    public CarritoResponseDTO findByUser(@PathVariable Long id) {
        return service.findByUser(id);
    }

    @PostMapping("/add/{idPostVenta}")
    public CarritoResponseDTO addProduct(@PathVariable Long idPostVenta) {
        return service.addProduct(idPostVenta);
    }

    @PostMapping("/remove")
    public CarritoResponseDTO removeProduct(@RequestBody ProductoCarritoRequestDTO dto) {
        return service.removeProduct(dto.getIdProducto());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
