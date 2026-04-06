package com.tfg.angel.gameswap.backend.business.service;

import com.tfg.angel.gameswap.backend.business.dto.request.IntercambioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.IntercambioResponseDTO;

import java.util.List;

public interface IntercambioService {

    IntercambioResponseDTO create(IntercambioRequestDTO dto);

    IntercambioResponseDTO findById(Long id);

    List<IntercambioResponseDTO> findAll();

    List<IntercambioResponseDTO> findByUsuario(Long idUsuario);

    void delete(Long id);
}
