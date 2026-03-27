package com.tfg.angel.gameswap.backend.business.service;

import com.tfg.angel.gameswap.backend.business.dto.request.ProductoRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.ProductoResponseDTO;

import java.util.List;

public interface ProductoService {

    ProductoResponseDTO crearProducto(ProductoRequestDTO dto);

    ProductoResponseDTO obtenerProductoPorId(Long id);

    List<ProductoResponseDTO> obtenerTodos();

    List<ProductoResponseDTO> buscarPorNombre(String nombre);

    List<ProductoResponseDTO> filtrarPorEstado(String estado);

    ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO dto);

    void eliminarProducto(Long id);
}
