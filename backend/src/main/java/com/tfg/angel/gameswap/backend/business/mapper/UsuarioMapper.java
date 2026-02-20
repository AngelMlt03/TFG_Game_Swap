package com.tfg.angel.gameswap.backend.business.mapper;

import com.tfg.angel.gameswap.backend.business.dto.UsuarioDTO;
import com.tfg.angel.gameswap.backend.business.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioMapper {

    public static UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail()
        );
    }

    public static Usuario toEntity(UsuarioDTO dto) {
        return new Usuario(
                dto.getId(),
                dto.getNombre(),
                dto.getEmail()
        );
    }
}
