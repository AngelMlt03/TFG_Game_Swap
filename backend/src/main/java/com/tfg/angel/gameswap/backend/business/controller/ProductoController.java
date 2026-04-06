package com.tfg.angel.gameswap.backend.business.controller;

import com.tfg.angel.gameswap.backend.business.dto.request.ProductoRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.ProductoResponseDTO;
import com.tfg.angel.gameswap.backend.business.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ProductoResponseDTO create(@RequestBody ProductoRequestDTO dto) {
        return productoService.crearProducto(dto);
    }

    @GetMapping("/{id}")
    public ProductoResponseDTO findById(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id);
    }

    @GetMapping
    public List<ProductoResponseDTO> findAll() {
        return productoService.obtenerTodos();
    }

    @GetMapping("/buscar")
    public List<ProductoResponseDTO> findByName(@RequestParam String nombre) {
        return productoService.buscarPorNombre(nombre);
    }

    @GetMapping("/estado")
    public List<ProductoResponseDTO> findByState(@RequestParam String estado) {
        return productoService.filtrarPorEstado(estado);
    }

    @PutMapping("/{id}")
    public ProductoResponseDTO update(@PathVariable Long id,
                                          @RequestBody ProductoRequestDTO dto) {
        return productoService.actualizarProducto(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productoService.eliminarProducto(id);
    }
}
