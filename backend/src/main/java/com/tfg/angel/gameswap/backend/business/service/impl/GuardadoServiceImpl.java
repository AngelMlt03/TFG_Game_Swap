package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.request.GuardadoRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostIntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostVentaResponseDTO;

import com.tfg.angel.gameswap.backend.business.mapper.PostIntercambioMapper;
import com.tfg.angel.gameswap.backend.business.mapper.PostVentaMapper;

import com.tfg.angel.gameswap.backend.business.model.Guardado;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.repository.GuardadoRepository;
import com.tfg.angel.gameswap.backend.business.repository.PostIntercambioRepository;
import com.tfg.angel.gameswap.backend.business.repository.PostVentaRepository;
import com.tfg.angel.gameswap.backend.business.service.GuardadoService;

import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuardadoServiceImpl implements GuardadoService {

    private final GuardadoRepository guardadoRepository;
    private final PostVentaRepository postVentaRepository;
    private final PostIntercambioRepository postIntercambioRepository;

    private final UsuarioDetailsService usuarioDetailsService;

    @Override
    public void guardar(GuardadoRequestDTO dto) {

        Usuario usuario = usuarioDetailsService.obtenerUsuarioActual();

        boolean existe = guardadoRepository.existsByIdUsuarioAndIdPostAndTipoPost(
                usuario.getId(),
                dto.getIdPost(),
                dto.getTipoPost()
        );

        if (existe) return;

        Guardado guardado = Guardado.builder()
                        .idUsuario(usuario.getId())
                        .idPost(dto.getIdPost())
                        .tipoPost(dto.getTipoPost())
                        .build();

        guardadoRepository.save(guardado);
    }

    @Override
    public void eliminar(Long idPost, String tipoPost)  {

        Usuario usuario = usuarioDetailsService.obtenerUsuarioActual();

        guardadoRepository.findByIdUsuarioAndIdPostAndTipoPost(usuario.getId(), idPost, tipoPost)
                .ifPresent(guardadoRepository::delete);
    }

    @Override
    public List<PostVentaResponseDTO> getVentasGuardadas() {

        Usuario usuario = usuarioDetailsService.obtenerUsuarioActual();

        List<Long> ids = guardadoRepository
                        .findByIdUsuarioAndTipoPost(
                                usuario.getId(),
                                "VENTA"
                        )
                        .stream()
                        .map(Guardado::getIdPost)
                        .toList();

        return postVentaRepository
                .findAllById(ids)
                .stream()
                .map(PostVentaMapper::toDTO)
                .toList();
    }

    @Override
    public List<PostIntercambioResponseDTO> getIntercambiosGuardados() {

        Usuario usuario = usuarioDetailsService.obtenerUsuarioActual();

        List<Long> ids = guardadoRepository
                        .findByIdUsuarioAndTipoPost(
                                usuario.getId(),
                                "INTERCAMBIO"
                        )
                        .stream()
                        .map(Guardado::getIdPost)
                        .toList();

        return postIntercambioRepository
                .findAllById(ids)
                .stream()
                .map(PostIntercambioMapper::toDTO)
                .toList();
    }

    @Override
    public boolean existe(Long idPost, String tipoPost) {

        Usuario usuario = usuarioDetailsService.obtenerUsuarioActual();

        return guardadoRepository.existsByIdUsuarioAndIdPostAndTipoPost(
                        usuario.getId(),
                        idPost,
                        tipoPost
                );
    }
}
