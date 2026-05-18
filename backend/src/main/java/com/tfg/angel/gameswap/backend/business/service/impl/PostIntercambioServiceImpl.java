package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.request.PostIntercambioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostIntercambioResponseDTO;
import com.tfg.angel.gameswap.backend.business.mapper.PostIntercambioMapper;
import com.tfg.angel.gameswap.backend.business.model.PostIntercambio;
import com.tfg.angel.gameswap.backend.business.model.Producto;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import com.tfg.angel.gameswap.backend.business.repository.PostIntercambioRepository;
import com.tfg.angel.gameswap.backend.business.repository.ProductoRepository;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.PostIntercambioService;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostIntercambioServiceImpl implements PostIntercambioService {

    private final PostIntercambioRepository postIntercambioRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioDetailsService usuarioDetailsService;
    private final ProductoRepository productoRepository;

    @Override
    public PostIntercambioResponseDTO create(PostIntercambioRequestDTO dto) {

        Usuario usuario = usuarioDetailsService.obtenerUsuarioActual();

        // PRODUCTO PROPIO

        Producto producto = Producto.builder()
                .nombre(dto.getNombreProducto())
                .idAPI(dto.getIdApi().intValue())
                .estado( EstadoProducto.valueOf(dto.getEstadoProducto()) )
                .build();

        productoRepository.save(producto);

        // PRODUCTO BUSCADO

        Producto productoCambio = Producto.builder()
                .nombre(dto.getNombreProductoIntercambio())
                .idAPI(dto.getIdApiIntercambio().intValue())
                .estado( EstadoProducto.valueOf(dto.getEstadoProductoIntercambio()) )
                .build();

        productoRepository.save(productoCambio);

        // POST

        PostIntercambio post =
                PostIntercambio.builder()
                        .usuario(usuario)
                        .producto(producto)
                        .productoCambio(productoCambio)
                        .plataforma(dto.getPlataforma())
                        .plataformaCambio(dto.getPlataformaIntercambio())
                        .descripcion(dto.getDescripcion())
                        .estado(EstadoPost.ACTIVO)
                        .build();

        postIntercambioRepository.save(post);

        return PostIntercambioMapper.toDTO(post);
    }

    @Override
    public List<PostIntercambioResponseDTO> findByUsuarioActivo() {
        return postIntercambioRepository
                .findByUsuarioIdAndEstado(usuarioDetailsService.obtenerUsuarioActual().getId(), EstadoPost.ACTIVO)
                .stream()
                .map(PostIntercambioMapper::toDTO)
                .toList();
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
    public List<PostIntercambioResponseDTO> findByEstado(EstadoPost estado) {
        return postIntercambioRepository.findByEstado(estado)
                .stream()
                .map(PostIntercambioMapper::toDTO)
                .toList();
    }

    @Override
    public PostIntercambioResponseDTO update(Long id, PostIntercambioRequestDTO dto) {

        PostIntercambio post = postIntercambioRepository.findById(id)
                .orElseThrow(() -> new GSNotFoundException("Post no encontrado"));

        Producto producto = post.getProducto();

        producto.setNombre(dto.getNombreProducto());
        producto.setIdAPI(dto.getIdApi().intValue());
        producto.setEstado(
                EstadoProducto.valueOf(dto.getEstadoProducto())
        );

        Producto productoCambio = post.getProductoCambio();

        productoCambio.setNombre(dto.getNombreProductoIntercambio());
        productoCambio.setIdAPI(dto.getIdApiIntercambio().intValue());
        productoCambio.setEstado(
                EstadoProducto.valueOf(dto.getEstadoProductoIntercambio())
        );

        productoRepository.save(producto);
        productoRepository.save(productoCambio);

        post.setPlataforma(dto.getPlataforma());
        post.setPlataformaCambio(dto.getPlataformaIntercambio());
        post.setDescripcion(dto.getDescripcion());

        postIntercambioRepository.save(post);

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
