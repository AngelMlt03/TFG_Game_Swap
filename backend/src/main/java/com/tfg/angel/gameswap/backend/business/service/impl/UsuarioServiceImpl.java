package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.UsuarioDTO;
import com.tfg.angel.gameswap.backend.business.mapper.UsuarioMapper;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.UsuarioService;
import com.tfg.angel.gameswap.backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll()
                .stream().map(UsuarioMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO findById(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent())
            return UsuarioMapper.toDTO(usuario.get());
        throw new ResourceNotFoundException("Usuario no encontrado");
    }

    @Override
    public UsuarioDTO insert(UsuarioDTO usuarioDTO) {
        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
        Usuario saved = usuarioRepository.save(usuario);
        return UsuarioMapper.toDTO(saved);
    }
}
