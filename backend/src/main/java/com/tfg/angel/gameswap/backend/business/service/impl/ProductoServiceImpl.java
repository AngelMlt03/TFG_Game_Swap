package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.request.ProductoRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.ProductoResponseDTO;
import com.tfg.angel.gameswap.backend.business.mapper.ProductoMapper;
import com.tfg.angel.gameswap.backend.business.model.Producto;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import com.tfg.angel.gameswap.backend.business.repository.ProductoRepository;
import com.tfg.angel.gameswap.backend.business.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public ProductoResponseDTO crearProducto(ProductoRequestDTO dto) {

        Producto producto = ProductoMapper.toEntity(dto);
        producto = productoRepository.save(producto);

        return ProductoMapper.toDTO(producto);
    }

    @Override
    public ProductoResponseDTO obtenerProductoPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        return ProductoMapper.toDTO(producto);
    }

    @Override
    public List<ProductoResponseDTO> obtenerTodos() {
        return productoRepository.findAll()
                .stream()
                .map(ProductoMapper::toDTO)
                .toList();
    }

    @Override
    public List<ProductoResponseDTO> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(ProductoMapper::toDTO)
                .toList();
    }

    @Override
    public List<ProductoResponseDTO> filtrarPorEstado(String estado) {

        EstadoProducto estadoEnum;

        try {
            estadoEnum = EstadoProducto.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado no válido");
        }

        return productoRepository.findByEstado(estadoEnum)
                .stream()
                .map(ProductoMapper::toDTO)
                .toList();
    }

    @Override
    public ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO dto) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setNombre(dto.getNombre());
        producto.setIdAPI(dto.getIdAPI());
        producto.setEstado(dto.getEstado());

        producto = productoRepository.save(producto);

        return ProductoMapper.toDTO(producto);
    }

    @Override
    public void eliminarProducto(Long id) {

        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }

        productoRepository.deleteById(id);
    }
}