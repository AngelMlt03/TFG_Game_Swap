package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.request.ChangePasswordRequest;
import com.tfg.angel.gameswap.backend.business.dto.request.UsuarioRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.*;
import com.tfg.angel.gameswap.backend.business.mapper.UsuarioMapper;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import com.tfg.angel.gameswap.backend.business.repository.UsuarioRepository;
import com.tfg.angel.gameswap.backend.business.service.PostIntercambioService;
import com.tfg.angel.gameswap.backend.business.service.PostVentaService;
import com.tfg.angel.gameswap.backend.business.service.ProductoService;
import com.tfg.angel.gameswap.backend.business.service.UsuarioService;
import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PostVentaService postVentaService;
    private final PostIntercambioService postIntercambioService;
    private final ProductoService productoService;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioDetailsService usuarioDetailsService;

    @Override
    public UsuarioResponseDTO create(UsuarioRequestDTO dto) {

        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new GSBadRequestException("El correo ya está en uso");
        }

        if (usuarioRepository.existsByNombreUsuario(dto.getNombreUsuario())) {
            throw new GSBadRequestException("El nombre de usuario ya existe");
        }

        Usuario usuario = UsuarioMapper.toEntity(dto);
        usuario = usuarioRepository.save(usuario);

        return UsuarioMapper.toDTO(usuario);
    }

    @Override
    public UsuarioResponseDTO findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new GSNotFoundException("Usuario no encontrado"));

        return UsuarioMapper.toDTO(usuario);
    }

    @Override
    public UsuarioResponseDTO findByUsername(String username) {
        var usuario = usuarioRepository
                .findByNombreUsuario(username)
                .orElseThrow();

        return UsuarioMapper.toDTO(usuario);
    }

    @Override
    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioMapper::toDTO)
                .toList();
    }

    @Override
    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO dto) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new GSNotFoundException("Usuario no encontrado"));

        usuario.setNombre(dto.getNombre());
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setCorreo(dto.getCorreo());

        usuario = usuarioRepository.save(usuario);

        return UsuarioMapper.toDTO(usuario);
    }

    @Override
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new GSNotFoundException("Usuario no encontrado");
        }

        usuarioRepository.deleteById(id);
    }

    @Override
    public void changePassword(String username, ChangePasswordRequest request) {

        var usuario = usuarioRepository.findByNombreUsuario(username).orElseThrow();

        if (!passwordEncoder.matches(request.currentPassword(), usuario.getPassword())) {
            throw new GSBadRequestException("Contraseña actual incorrecta");
        }

        usuario.setPassword(passwordEncoder.encode(request.newPassword()));

        usuarioRepository.save(usuario);
    }

    @Override
    public ResponseEntity<Double> addSaldo(Double cantidad) {

        Usuario usuario = usuarioDetailsService.obtenerUsuarioActual();

        usuario.setSaldo( usuario.getSaldo() + cantidad );
        usuarioRepository.save(usuario);

        return ResponseEntity.ok( usuario.getSaldo() );
    }

    @Override
    public PerfilPublicoDTO getPerfilPublico(String nombreUsuario) {
        Usuario usuario = usuarioRepository
                .findByNombreUsuarioIgnoreCase(nombreUsuario)
                .orElse(null);

        assert usuario != null;
        return PerfilPublicoDTO.builder()
                .nombre(usuario.getNombre())
                .nombreUsuario(usuario.getNombreUsuario())
                .correo(usuario.getCorreo())
                .estrellas(usuario.getEstrellas())
                .build();
    }

    public List<PostBusquedaDTO> findVentasByUsuario(String usuario){

        List<PostBusquedaDTO> resultado = new ArrayList<>();

        UsuarioResponseDTO usuarioActual = findByUsername(usuario);

        postVentaService.findByEstado(EstadoPost.ACTIVO)
                .stream()
                .filter(p -> p.getIdVendedor().equals(usuarioActual.getId()))
                .forEach(p -> {
                            ProductoResponseDTO producto = productoService.findById(p.getIdProducto());
                            resultado.add( getPostVenta(p, producto) );
                        }
                );

        return resultado;
    }

    public List<PostBusquedaDTO> findIntercambiosByUsuario(String usuario){

        List<PostBusquedaDTO> resultado = new ArrayList<>();

        UsuarioResponseDTO usuarioActual = findByUsername(usuario);

        postIntercambioService.findByEstado(EstadoPost.ACTIVO)
                .stream()
                .filter(p -> p.getIdUsuario().equals(usuarioActual.getId()))
                .forEach(p -> {
                            ProductoResponseDTO producto = productoService.findById(p.getIdProducto());
                            ProductoResponseDTO productoIntercambio = productoService.findById(p.getIdProductoCambio());
                            resultado.add( getIntercambio(p, producto, productoIntercambio) );
                        }
                );

        return resultado;
    }

    private static PostBusquedaDTO getPostVenta(PostVentaResponseDTO p, ProductoResponseDTO producto) {
        return PostBusquedaDTO.builder()
                .id(p.getId())
                .tipo("VENTA")

                .idApi(producto.getIdAPI().longValue())
                .nombreProducto(p.getNombreProducto())
                .plataforma(p.getPlataforma())
                .estado(String.valueOf(producto.getEstado()))

                .precio(p.getPrecio())

                .nombreUsuario(p.getNombreUsuario())
                .descripcion(p.getDescripcion())

                .build();
    }

    private static PostBusquedaDTO getIntercambio(PostIntercambioResponseDTO p, ProductoResponseDTO producto, ProductoResponseDTO productoIntercambio) {
        return PostBusquedaDTO.builder()
                .id(p.getId())
                .tipo("INTERCAMBIO")

                .idApi(producto.getIdAPI().longValue())
                .nombreProducto(p.getNombreProducto())
                .plataforma(p.getPlataforma())
                .estado(String.valueOf(producto.getEstado()))

                .idApiIntercambio(productoIntercambio.getIdAPI().longValue())
                .nombreProductoIntercambio(p.getNombreProductoCambio())
                .plataformaIntercambio(p.getPlataformaCambio())
                .estadoIntercambio(String.valueOf(productoIntercambio.getEstado()))

                .nombreUsuario(p.getNombreUsuario())
                .descripcion(p.getDescripcion())

                .build();
    }
}
