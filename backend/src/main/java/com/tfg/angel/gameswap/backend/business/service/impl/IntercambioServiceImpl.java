package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.response.IntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.model.PostIntercambio;
import com.tfg.angel.gameswap.backend.business.repository.PostIntercambioRepository;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import com.tfg.angel.gameswap.backend.business.mapper.IntercambioMapper;
import com.tfg.angel.gameswap.backend.business.model.Intercambio;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.repository.IntercambioRepository;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.IntercambioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IntercambioServiceImpl implements IntercambioService {

    private final IntercambioRepository intercambioRepository;
    private final PostIntercambioRepository postIntercambioRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public IntercambioResponseDTO create(Long idPostIntercambio, Long idUsuarioCambio) {

        PostIntercambio post = postIntercambioRepository.findById(idPostIntercambio)
                .orElseThrow(() -> new GSNotFoundException("PostIntercambio no encontrado"));

        Usuario usuarioCambio = usuarioRepository.findById(idUsuarioCambio)
                .orElseThrow(() -> new GSNotFoundException("Usuario no encontrado"));

        Intercambio intercambio = Intercambio.builder()
                .postIntercambio(post)
                .usuarioCambio(usuarioCambio)
                .fecha(LocalDate.now())
                .build();

        return IntercambioMapper.toDTO(intercambioRepository.save(intercambio));
    }

    @Override
    public IntercambioResponseDTO findById(Long id) {
        Intercambio entity = intercambioRepository.findById(id)
                .orElseThrow(() -> new GSNotFoundException("Intercambio no encontrado"));

        return IntercambioMapper.toDTO(entity);
    }

    @Override
    public List<IntercambioResponseDTO> findAll() {
        return intercambioRepository.findAll()
                .stream()
                .map(IntercambioMapper::toDTO)
                .toList();
    }

    @Override
    public List<IntercambioResponseDTO> findByUsuario(Long idUsuario) {
        return intercambioRepository.findByPostIntercambioUsuarioId(idUsuario)
                .stream()
                .map(IntercambioMapper::toDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!intercambioRepository.existsById(id)) {
            throw new GSNotFoundException("Intercambio no encontrado");
        }

        intercambioRepository.deleteById(id);
    }
}
