package com.tfg.angel.gameswap.backend.business.service;

import com.tfg.angel.gameswap.backend.business.dto.request.PostVentaRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;

import java.util.List;

public interface PostVentaService {

    PostVentaResponseDTO create(PostVentaRequestDTO dto);

    PostVentaResponseDTO findById(Long id);

    List<PostVentaResponseDTO> findAll();

    List<PostVentaResponseDTO> findBySeller(Long idVendedor);

    List<PostVentaResponseDTO> findByProduct(Long idProducto);

    List<PostVentaResponseDTO> findByEstado(EstadoPost estado);

    PostVentaResponseDTO update(Long id, PostVentaRequestDTO dto);

    void delete(Long id);
}
