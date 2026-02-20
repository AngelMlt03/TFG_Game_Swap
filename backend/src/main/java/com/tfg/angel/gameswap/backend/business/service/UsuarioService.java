package com.tfg.angel.gameswap.backend.business.service;

import com.tfg.angel.gameswap.backend.business.dto.UsuarioDTO;

import java.util.List;

public interface UsuarioService {

    List<UsuarioDTO> findAll();

    UsuarioDTO findById(Long id);

    UsuarioDTO insert(UsuarioDTO usuarioDTO);
}
