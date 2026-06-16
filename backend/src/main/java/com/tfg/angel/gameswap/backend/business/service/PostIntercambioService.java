package com.tfg.angel.gameswap.backend.business.service;

import com.tfg.angel.gameswap.backend.business.dto.request.PostIntercambioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.request.PostVentaRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostIntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;

import java.util.List;

public interface PostIntercambioService {

    PostIntercambioResponseDTO create(PostIntercambioRequestDTO dto);

    List<PostIntercambioResponseDTO> findByUsuarioActivo();

    PostIntercambioResponseDTO findById(Long id);

    List<PostIntercambioResponseDTO> findAll();

    List<PostIntercambioResponseDTO> findByUser(Long idUsuario);

    List<PostIntercambioResponseDTO> findByProduct(Long idProducto);

    List<PostIntercambioResponseDTO> findByEstado(EstadoPost estado);

    PostIntercambioResponseDTO update(Long id, PostIntercambioRequestDTO dto);

    void convertirIntercambioAVenta(Long id, PostVentaRequestDTO dto);

    boolean existeIntercambioInverso(String tuJuego, String juegoBuscado);

    void delete(Long id);
}
