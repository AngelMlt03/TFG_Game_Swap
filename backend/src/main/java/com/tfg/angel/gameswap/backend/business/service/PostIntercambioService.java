package com.tfg.angel.gameswap.backend.business.service;

import com.tfg.angel.gameswap.backend.business.dto.request.PostIntercambioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostIntercambioResponseDTO;

import java.util.List;

public interface PostIntercambioService {

    PostIntercambioResponseDTO create(PostIntercambioRequestDTO dto);

    PostIntercambioResponseDTO findById(Long id);

    List<PostIntercambioResponseDTO> findAll();

    List<PostIntercambioResponseDTO> findByUser(Long idUsuario);

    List<PostIntercambioResponseDTO> findByProduct(Long idProducto);

    PostIntercambioResponseDTO update(Long id, PostIntercambioRequestDTO dto);

    void delete(Long id);
}
