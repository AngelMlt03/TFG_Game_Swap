package com.tfg.angel.gameswap.backend.business.service;

import com.tfg.angel.gameswap.backend.business.dto.request.GuardadoRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostIntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostVentaResponseDTO;

import java.util.List;

public interface GuardadoService {

    void guardar(GuardadoRequestDTO dto);

    void eliminar(Long idPost, String tipoPost);

    List<PostVentaResponseDTO> getVentasGuardadas();

    List<PostIntercambioResponseDTO> getIntercambiosGuardados();

    boolean existe(Long idPost, String tipoPost);
}
