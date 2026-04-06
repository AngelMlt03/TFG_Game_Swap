package com.tfg.angel.gameswap.backend.business.service;

import com.tfg.angel.gameswap.backend.business.dto.request.PostVentaRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostVentaResponseDTO;

import java.util.List;

public interface PostVentaService {

    PostVentaResponseDTO create(PostVentaRequestDTO dto);

    PostVentaResponseDTO findById(Long id);

    List<PostVentaResponseDTO> findAll();

    List<PostVentaResponseDTO> findBySeller(Long idVendedor);

    List<PostVentaResponseDTO> findByproduct(Long idProducto);

    PostVentaResponseDTO update(Long id, PostVentaRequestDTO dto);

    void delete(Long id);
}
