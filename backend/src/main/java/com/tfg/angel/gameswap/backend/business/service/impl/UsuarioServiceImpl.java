package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.request.UsuarioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.UsuarioResponseDTO;
import com.tfg.angel.gameswap.backend.business.mapper.UsuarioMapper;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.UsuarioService;
import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UsuarioResponseDTO create(UsuarioRequestDTO dto) {

        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new GSBadRequestException("El correo ya está en uso");
        }

        if (usuarioRepository.existsByNUsuario(dto.getNUsuario())) {
            throw new GSBadRequestException("El nombre de usuario ya existe");
        }

        Usuario usuario = UsuarioMapper.toEntity(dto);
        usuario = usuarioRepository.save(usuario);

        return UsuarioMapper.toDTO(usuario);
    }

    @Override
    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new GSNotFoundException("Usuario no encontrado"));

        return UsuarioMapper.toDTO(usuario);
    }

    @Override
    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioMapper::toDTO)
                .toList();
    }

    @Override
    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO dto) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new GSNotFoundException("Usuario no encontrado"));

        usuario.setNombre(dto.getNombre());
        usuario.setNUsuario(dto.getNUsuario());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setCorreo(dto.getCorreo());

        usuario = usuarioRepository.save(usuario);

        return UsuarioMapper.toDTO(usuario);
    }

    @Override
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new GSNotFoundException("Usuario no encontrado");
        }

        usuarioRepository.deleteById(id);
    }
}
