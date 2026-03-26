package com.tfg.angel.gameswap.backend.business.service;

import com.tfg.angel.gameswap.backend.business.dto.request.UsuarioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {

    UsuarioResponseDTO crearUsuario(UsuarioRequestDTO dto);

    UsuarioResponseDTO obtenerUsuarioPorId(Long id);

    List<UsuarioResponseDTO> obtenerTodos();

    UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO dto);

    void eliminarUsuario(Long id);
}
