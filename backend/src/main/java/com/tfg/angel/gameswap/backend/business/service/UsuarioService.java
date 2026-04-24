package com.tfg.angel.gameswap.backend.business.service;

import com.tfg.angel.gameswap.backend.business.dto.request.ChangePasswordRequest;
import com.tfg.angel.gameswap.backend.business.dto.request.UsuarioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {

    UsuarioResponseDTO create(UsuarioRequestDTO dto);

    UsuarioResponseDTO findById(Long id);

    UsuarioResponseDTO findByUsername(String username);

    List<UsuarioResponseDTO> findAll();

    UsuarioResponseDTO update(Long id, UsuarioRequestDTO dto);

    void delete(Long id);

    void changePassword(String username, ChangePasswordRequest request);
}
