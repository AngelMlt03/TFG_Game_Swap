package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.request.PostIntercambioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostIntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.mapper.PostIntercambioMapper;
import com.tfg.angel.gameswap.backend.business.model.PostIntercambio;
import com.tfg.angel.gameswap.backend.business.model.Producto;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.repository.PostIntercambioRepository;
import com.tfg.angel.gameswap.backend.business.repository.ProductoRepository;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.PostIntercambioService;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostIntercambioServiceImpl implements PostIntercambioService {

    private final PostIntercambioRepository postIntercambioRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Override
    public PostIntercambioResponseDTO create(PostIntercambioRequestDTO dto) {

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new GSNotFoundException("Usuario no encontrado"));

        Producto producto = productoRepository.findById(dto.getIdProducto())
                .orElseThrow(() -> new GSNotFoundException("Producto ofrecido no encontrado"));

        Producto productoCambio = productoRepository.findById(dto.getIdProductoCambio())
                .orElseThrow(() -> new GSNotFoundException("Producto deseado no encontrado"));

        PostIntercambio post = PostIntercambio.builder()
                .usuario(usuario)
                .producto(producto)
                .productoCambio(productoCambio)
                .build();

        post = postIntercambioRepository.save(post);

        return PostIntercambioMapper.toDTO(post);
    }

    @Override
    public PostIntercambioResponseDTO findById(Long id) {
        PostIntercambio post = postIntercambioRepository.findById(id)
                .orElseThrow(() -> new GSNotFoundException("Post no encontrado"));

        return PostIntercambioMapper.toDTO(post);
    }

    @Override
    public List<PostIntercambioResponseDTO> findAll() {
        return postIntercambioRepository.findAll()
                .stream()
                .map(PostIntercambioMapper::toDTO)
                .toList();
    }

    @Override
    public List<PostIntercambioResponseDTO> findByUser(Long idUsuario) {
        return postIntercambioRepository.findByUsuarioId(idUsuario)
                .stream()
                .map(PostIntercambioMapper::toDTO)
                .toList();
    }

    @Override
    public List<PostIntercambioResponseDTO> findByProduct(Long idProducto) {
        return postIntercambioRepository.findByProductoId(idProducto)
                .stream()
                .map(PostIntercambioMapper::toDTO)
                .toList();
    }

    @Override
    public PostIntercambioResponseDTO update(Long id, PostIntercambioRequestDTO dto) {

        PostIntercambio post = postIntercambioRepository.findById(id)
                .orElseThrow(() -> new GSNotFoundException("Post no encontrado"));

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new GSNotFoundException("Usuario no encontrado"));

        Producto producto = productoRepository.findById(dto.getIdProducto())
                .orElseThrow(() -> new GSNotFoundException("Producto ofrecido no encontrado"));

        Producto productoCambio = productoRepository.findById(dto.getIdProductoCambio())
                .orElseThrow(() -> new GSNotFoundException("Producto deseado no encontrado"));

        post.setUsuario(usuario);
        post.setProducto(producto);
        post.setProductoCambio(productoCambio);

        post = postIntercambioRepository.save(post);

        return PostIntercambioMapper.toDTO(post);
    }

    @Override
    public void delete(Long id) {
        if (!postIntercambioRepository.existsById(id)) {
            throw new GSNotFoundException("Post no encontrado");
        }

        postIntercambioRepository.deleteById(id);
    }
}
