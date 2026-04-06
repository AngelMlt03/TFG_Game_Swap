package com.tfg.angel.gameswap.backend.business.service;

import com.tfg.angel.gameswap.backend.business.dto.request.ProductoRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.ProductoResponseDTO;

import java.util.List;

public interface ProductoService {

    ProductoResponseDTO create(ProductoRequestDTO dto);

    ProductoResponseDTO findById(Long id);

    List<ProductoResponseDTO> findAll();

    List<ProductoResponseDTO> findByName(String nombre);

    List<ProductoResponseDTO> findByState(String estado);

    ProductoResponseDTO update(Long id, ProductoRequestDTO dto);

    void delete(Long id);
}
