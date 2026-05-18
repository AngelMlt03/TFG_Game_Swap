package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.request.PostVentaRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostBusquedaDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.PostVentaResponseDTO;
import com.tfg.angel.gameswap.backend.business.mapper.PostVentaMapper;
import com.tfg.angel.gameswap.backend.business.model.PostVenta;
import com.tfg.angel.gameswap.backend.business.model.Producto;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoProducto;
import com.tfg.angel.gameswap.backend.business.repository.PostVentaRepository;
import com.tfg.angel.gameswap.backend.business.repository.ProductoRepository;
import com.tfg.angel.gameswap.backend.business.service.PostVentaService;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostVentaServiceImpl implements PostVentaService {

    private final PostVentaRepository postVentaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioDetailsService usuarioDetailsService;

    @Override
    public PostVentaResponseDTO create(PostVentaRequestDTO dto) {

        Usuario vendedor = usuarioDetailsService.obtenerUsuarioActual();

        Producto producto = Producto.builder()
                .idAPI(dto.getIdApi().intValue())
                .nombre(dto.getNombreProducto())
                .estado(EstadoProducto.valueOf(dto.getEstadoProducto()))
                .build();

        producto = productoRepository.save(producto);

        PostVenta post = PostVenta.builder()
                .vendedor(vendedor)
                .producto(producto)
                .plataforma(dto.getPlataforma())
                .precio(dto.getPrecio())
                .estado(EstadoPost.ACTIVO)
                .descripcion(dto.getDescripcion())
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
    public List<PostVentaResponseDTO> findByEstado(EstadoPost estado) {

        return postVentaRepository.findByEstado(estado)
                .stream()
                .map(PostVentaMapper::toDTO)
                .toList();
    }

    @Override
    public PostVentaResponseDTO update(Long id, PostVentaRequestDTO dto) {

        PostVenta post = postVentaRepository.findById(id)
                .orElseThrow(() -> new GSNotFoundException("Post no encontrado"));

        Producto producto = post.getProducto();

        producto.setNombre(dto.getNombreProducto());
        producto.setIdAPI(dto.getIdApi().intValue());
        producto.setEstado(
                EstadoProducto.valueOf(dto.getEstadoProducto())
        );

        productoRepository.save(producto);

        post.setPrecio(dto.getPrecio());
        post.setPlataforma(dto.getPlataforma());
        post.setDescripcion(dto.getDescripcion());

        postVentaRepository.save(post);

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
