package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.request.ProductoRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.ProductoResponseDTO;
import com.tfg.angel.gameswap.backend.business.mapper.ProductoMapper;
import com.tfg.angel.gameswap.backend.business.model.Producto;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import com.tfg.angel.gameswap.backend.business.repository.ProductoRepository;
import com.tfg.angel.gameswap.backend.business.service.ProductoService;
import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public ProductoResponseDTO create(ProductoRequestDTO dto) {

        Producto producto = ProductoMapper.toEntity(dto);
        producto = productoRepository.save(producto);

        return ProductoMapper.toDTO(producto);
    }

    @Override
    public ProductoResponseDTO findById(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new GSNotFoundException("Producto no encontrado"));

        return ProductoMapper.toDTO(producto);
    }

    @Override
    public List<ProductoResponseDTO> findAll() {
        return productoRepository.findAll()
                .stream()
                .map(ProductoMapper::toDTO)
                .toList();
    }

    @Override
    public List<ProductoResponseDTO> findByName(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(ProductoMapper::toDTO)
                .toList();
    }

    @Override
    public List<ProductoResponseDTO> findByState(String estado) {

        EstadoProducto estadoEnum;

        try {
            estadoEnum = EstadoProducto.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new GSBadRequestException("Estado no válido");
        }

        return productoRepository.findByEstado(estadoEnum)
                .stream()
                .map(ProductoMapper::toDTO)
                .toList();
    }

    @Override
    public ProductoResponseDTO update(Long id, ProductoRequestDTO dto) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new GSNotFoundException("Producto no encontrado"));

        producto.setNombre(dto.getNombre());
        producto.setIdAPI(dto.getIdAPI());
        producto.setEstado(dto.getEstado());

        producto = productoRepository.save(producto);

        return ProductoMapper.toDTO(producto);
    }

    @Override
    public void delete(Long id) {

        if (!productoRepository.existsById(id)) {
            throw new GSNotFoundException("Producto no encontrado");
        }

        productoRepository.deleteById(id);
    }
}