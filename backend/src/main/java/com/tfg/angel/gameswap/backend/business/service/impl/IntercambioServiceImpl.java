package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.request.IntercambioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.IntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import com.tfg.angel.gameswap.backend.business.mapper.IntercambioMapper;
import com.tfg.angel.gameswap.backend.business.model.Intercambio;
import com.tfg.angel.gameswap.backend.business.model.Producto;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.repository.IntercambioRepository;
import com.tfg.angel.gameswap.backend.business.repository.ProductoRepository;
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
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public IntercambioResponseDTO create(IntercambioRequestDTO dto) {

        Producto producto = productoRepository.findById(dto.getIdProducto())
                .orElseThrow(() -> new GSNotFoundException("Producto no encontrado"));

        Producto cambio = productoRepository.findById(dto.getIdCambio())
                .orElseThrow(() -> new GSNotFoundException("Producto de cambio no encontrado"));

        Usuario usuarioProducto = usuarioRepository.findById(dto.getIdUsuarioProducto())
                .orElseThrow(() -> new GSNotFoundException("Usuario producto no encontrado"));

        Usuario usuarioCambio = usuarioRepository.findById(dto.getIdUsuarioCambio())
                .orElseThrow(() -> new GSNotFoundException("Usuario cambio no encontrado"));

        if (usuarioProducto.getId().equals(usuarioCambio.getId())) {
            throw new GSBadRequestException("Los usuarios no pueden ser el mismo");
        }

        Intercambio entity = Intercambio.builder()
                .producto(producto)
                .productoCambio(cambio)
                .usuarioProducto(usuarioProducto)
                .usuarioCambio(usuarioCambio)
                .fecha(LocalDate.now())
                .build();

        entity = intercambioRepository.save(entity);

        return IntercambioMapper.toDTO(entity);
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
        return intercambioRepository.findByUsuarioProductoId(idUsuario)
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
