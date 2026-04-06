package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.request.PostVentaRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.mapper.PostVentaMapper;
import com.tfg.angel.gameswap.backend.business.model.PostVenta;
import com.tfg.angel.gameswap.backend.business.model.Producto;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.repository.PostVentaRepository;
import com.tfg.angel.gameswap.backend.business.repository.ProductoRepository;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.PostVentaService;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostVentaServiceImpl implements PostVentaService {

    private final PostVentaRepository postVentaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Override
    public PostVentaResponseDTO create(PostVentaRequestDTO dto) {

        Usuario vendedor = usuarioRepository.findById(dto.getIdVendedor())
                .orElseThrow(() -> new GSNotFoundException("Vendedor no encontrado"));

        Producto producto = productoRepository.findById(dto.getIdProducto())
                .orElseThrow(() -> new GSNotFoundException("Producto no encontrado"));

        PostVenta post = PostVenta.builder()
                .vendedor(vendedor)
                .producto(producto)
                .precio(dto.getPrecio())
                .build();

        post = postVentaRepository.save(post);

        return PostVentaMapper.toDTO(post);
    }

    @Override
    public PostVentaResponseDTO findById(Long id) {
        PostVenta post = postVentaRepository.findById(id)
                .orElseThrow(() -> new GSNotFoundException("Post no encontrado"));

        return PostVentaMapper.toDTO(post);
    }

    @Override
    public List<PostVentaResponseDTO> findAll() {
        return postVentaRepository.findAll()
                .stream()
                .map(PostVentaMapper::toDTO)
                .toList();
    }

    @Override
    public List<PostVentaResponseDTO> findBySeller(Long idVendedor) {
        return postVentaRepository.findByVendedorId(idVendedor)
                .stream()
                .map(PostVentaMapper::toDTO)
                .toList();
    }

    @Override
    public List<PostVentaResponseDTO> findByProduct(Long idProducto) {
        return postVentaRepository.findByProductoId(idProducto)
                .stream()
                .map(PostVentaMapper::toDTO)
                .toList();
    }

    @Override
    public PostVentaResponseDTO update(Long id, PostVentaRequestDTO dto) {

        PostVenta post = postVentaRepository.findById(id)
                .orElseThrow(() -> new GSNotFoundException("Post no encontrado"));

        Usuario vendedor = usuarioRepository.findById(dto.getIdVendedor())
                .orElseThrow(() -> new GSNotFoundException("Vendedor no encontrado"));

        Producto producto = productoRepository.findById(dto.getIdProducto())
                .orElseThrow(() -> new GSNotFoundException("Producto no encontrado"));

        post.setVendedor(vendedor);
        post.setProducto(producto);
        post.setPrecio(dto.getPrecio());

        post = postVentaRepository.save(post);

        return PostVentaMapper.toDTO(post);
    }

    @Override
    public void delete(Long id) {
        if (!postVentaRepository.existsById(id)) {
            throw new GSNotFoundException("Post no encontrado");
        }

        postVentaRepository.deleteById(id);
    }
}
