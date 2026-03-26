package com.tfg.angel.gameswap.backend.business.mapper;

import com.tfg.angel.gameswap.backend.business.dto.request.UsuarioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.UsuarioResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.Usuario;

public class UsuarioMapper {
    private UsuarioMapper() { }

    public static Usuario toEntity(UsuarioRequestDTO dto) {
        return Usuario.builder()
                .nombre(dto.getNombre())
                .nUsuario(dto.getNUsuario())
                .fechaNacimiento(dto.getFechaNacimiento())
                .correo(dto.getCorreo())
                .saldo(0.0)
                .estrellas(0.0)
                .build();
    }

    public static UsuarioResponseDTO toDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .nUsuario(usuario.getNUsuario())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .saldo(usuario.getSaldo())
                .correo(usuario.getCorreo())
                .estrellas(usuario.getEstrellas())
                .build();
    }
}
