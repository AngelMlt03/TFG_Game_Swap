package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.request.CompraVentaRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.CompraVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.mapper.CompraVentaMapper;
import com.tfg.angel.gameswap.backend.business.model.CompraVenta;
import com.tfg.angel.gameswap.backend.business.model.Producto;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.repository.CompraVentaRepository;
import com.tfg.angel.gameswap.backend.business.repository.ProductoRepository;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.CompraVentaService;
import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraVentaServiceImpl implements CompraVentaService {

    private final CompraVentaRepository compraVentaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Override
    public CompraVentaResponseDTO create(CompraVentaRequestDTO dto) {

        Usuario comprador = usuarioRepository.findById(dto.getIdComprador())
                .orElseThrow(() -> new GSNotFoundException("Comprador no encontrado"));

        Usuario vendedor = usuarioRepository.findById(dto.getIdVendedor())
                .orElseThrow(() -> new GSNotFoundException("Vendedor no encontrado"));

        Producto producto = productoRepository.findById(dto.getIdProducto())
                .orElseThrow(() -> new GSNotFoundException("Producto no encontrado"));

        if (comprador.getId().equals(vendedor.getId())) {
            throw new GSBadRequestException("El comprador y vendedor no pueden ser el mismo");
        }

        if (comprador.getSaldo() < dto.getPrecio()) {
            throw new GSBadRequestException("Saldo insuficiente");
        }

        comprador.setSaldo(comprador.getSaldo() - dto.getPrecio());
        vendedor.setSaldo(vendedor.getSaldo() + dto.getPrecio());

        CompraVenta entity = CompraVenta.builder()
                .comprador(comprador)
                .vendedor(vendedor)
                .producto(producto)
                .precio(dto.getPrecio())
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
        return compraVentaRepository.findByVendedorId(idVendedor)
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
