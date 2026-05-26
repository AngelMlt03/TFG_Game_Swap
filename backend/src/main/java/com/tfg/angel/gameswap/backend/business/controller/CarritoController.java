package com.tfg.angel.gameswap.backend.business.controller;

import com.tfg.angel.gameswap.backend.business.dto.response.ProductoCarritoResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService service;

    @PostMapping
    public void create() { service.create(); }

    @GetMapping("/usuario/{id}")
    public void findByUser(@PathVariable Long id) {
        service.findByUser(id);
    }

    @GetMapping
    public List<ProductoCarritoResponseDTO> getCarrito() {
        return service.getCarrito();
    }

    @PostMapping("/{idPostVenta}")
    public void agregarProducto(@PathVariable Long idPostVenta) {
        service.agregarProducto(idPostVenta);
    }

    @DeleteMapping("/{idPostVenta}")
    public void eliminarProducto(@PathVariable Long idPostVenta) {
        service.eliminarProducto(idPostVenta);
    }

    @GetMapping("/exists/{ventaId}")
    public boolean existe(@PathVariable Long ventaId) {
        return service.estaEnCarrito(ventaId);
    }

    @GetMapping("/precio")
    public Double precioCarrito() {
        return service.getPrecioCarrito();
    }

    @DeleteMapping("/vaciar")
    public void vaciar() { service.vaciar(); }
}
