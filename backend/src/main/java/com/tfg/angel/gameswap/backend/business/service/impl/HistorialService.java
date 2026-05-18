package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.response.CompraVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.IntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.mapper.CompraVentaMapper;
import com.tfg.angel.gameswap.backend.business.mapper.IntercambioMapper;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.repository.CompraVentaRepository;
import com.tfg.angel.gameswap.backend.business.repository.IntercambioRepository;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistorialService {

    private final CompraVentaRepository compraVentaRepository;
    private final IntercambioRepository intercambioRepository;
    private final UsuarioDetailsService usuarioDetailsService;

    public List<CompraVentaResponseDTO> getHistorialCompras() {

        Usuario usuario = usuarioDetailsService.obtenerUsuarioActual();

        return compraVentaRepository
                .findByCompradorId(usuario.getId())
                .stream()
                .map(CompraVentaMapper::toDTO)
                .toList();
    }

    public List<CompraVentaResponseDTO> getHistorialVentas() {

        Usuario usuario = usuarioDetailsService.obtenerUsuarioActual();

        return compraVentaRepository
                .findAll()
                .stream()
                .filter(cv ->
                        cv.getPostVenta() != null &&
                                cv.getPostVenta().getVendedor() != null &&
                                cv.getPostVenta().getVendedor().getId()
                                        .equals(usuario.getId())
                )
                .map(CompraVentaMapper::toDTO)
                .toList();
    }

    public List<IntercambioResponseDTO> getHistorialIntercambios() {

        Usuario usuario = usuarioDetailsService.obtenerUsuarioActual();

        return intercambioRepository
                .findAll()
                .stream()
                .filter(i ->
                        (
                                i.getPostIntercambio() != null &&
                                i.getPostIntercambio().getUsuario() != null &&
                                i.getPostIntercambio().getUsuario().getId().equals(usuario.getId())
                        )
                        ||
                        (
                                i.getUsuarioCambio() != null &&
                                i.getUsuarioCambio().getId() .equals(usuario.getId())
                        )
                )
                .map(IntercambioMapper::toDTO)
                .toList();
    }
}