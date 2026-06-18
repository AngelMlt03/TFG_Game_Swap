package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.response.CompraVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.mapper.CompraVentaMapper;
import com.tfg.angel.gameswap.backend.business.model.CompraVenta;
import com.tfg.angel.gameswap.backend.business.model.PostVenta;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.repository.CompraVentaRepository;
import com.tfg.angel.gameswap.backend.business.repository.PostVentaRepository;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.CompraVentaService;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraVentaServiceImpl implements CompraVentaService {

    private final CompraVentaRepository compraVentaRepository;
    private final PostVentaRepository postVentaRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioDetailsService usuarioDetailsService;

    @Override
    public CompraVentaResponseDTO create(Long idPostVenta, Long idUsuario) {

        Usuario comprador = usuarioRepository.findById(usuarioDetailsService.obtenerUsuarioActual().getId())
                .orElseThrow(() -> new GSNotFoundException("Usuario no encontrado"));

        PostVenta postVenta = postVentaRepository.findById(idPostVenta)
                .orElseThrow(() -> new GSNotFoundException("PostVenta no encontrado"));

        CompraVenta entity = CompraVenta.builder()
                .postVenta(postVenta)
                .comprador(comprador)
                .precio(postVenta.getPrecio())
                .fecha(LocalDate.now())
                .build();

        entity = compraVentaRepository.save(entity);

        return CompraVentaMapper.toDTO(entity);
    }

    @Override
    public CompraVentaResponseDTO findById(Long id) {
        CompraVenta entity = compraVentaRepository.findById(id)
                .orElseThrow(() -> new GSNotFoundException("CompraVenta no encontrada"));

        return CompraVentaMapper.toDTO(entity);
    }

    @Override
    public List<CompraVentaResponseDTO> findAll() {
        return compraVentaRepository.findAll()
                .stream()
                .map(CompraVentaMapper::toDTO)
                .toList();
    }

    @Override
    public List<CompraVentaResponseDTO> findByComprador(Long idComprador) {
        return compraVentaRepository.findByCompradorId(idComprador)
                .stream()
                .map(CompraVentaMapper::toDTO)
                .toList();
    }

    @Override
    public List<CompraVentaResponseDTO> findByVendedor(Long idVendedor) {
        return compraVentaRepository.findByPostVentaVendedorId(idVendedor)
                .stream()
                .map(CompraVentaMapper::toDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!compraVentaRepository.existsById(id)) {
            throw new GSNotFoundException("CompraVenta no encontrada");
        }

        compraVentaRepository.deleteById(id);
    }
}
