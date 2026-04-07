package com.tfg.angel.gameswap.backend.business.service;

import com.tfg.angel.gameswap.backend.business.dto.request.CarritoRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.CarritoResponseDTO;

public interface CarritoService {

    CarritoResponseDTO create(CarritoRequestDTO dto);

    CarritoResponseDTO findByUser(Long idUsuario);

    CarritoResponseDTO addProduct(Long idPostVenta, Long idUsuario);

    CarritoResponseDTO removeProduct(Long idProductoCarrito);

    void delete(Long id);
}
