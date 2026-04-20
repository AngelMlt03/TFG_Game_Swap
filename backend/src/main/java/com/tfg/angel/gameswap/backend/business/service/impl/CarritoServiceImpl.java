package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.request.CarritoRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.request.ProductoCarritoRequestDTO;
import com.tfg.angel.gameswap.backend.business.dto.response.CarritoResponseDTO;
import com.tfg.angel.gameswap.backend.business.mapper.CarritoMapper;
import com.tfg.angel.gameswap.backend.business.model.*;
import com.tfg.angel.gameswap.backend.business.repository.*;
import com.tfg.angel.gameswap.backend.business.service.CarritoService;
import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import com.tfg.angel.gameswap.backend.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PostVentaRepository postVentaRepository;
    private final ProductoCarritoRepository productoCarritoRepository;

    @Override
    public CarritoResponseDTO create(CarritoRequestDTO dto) {

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new GSNotFoundException("Usuario no encontrado"));

        Carrito carrito = Carrito.builder()
                .usuario(usuario)
                .coste(0.0)
                .build();

        carrito = carritoRepository.save(carrito);

        return CarritoMapper.toDTO(carrito);
    }

    @Override
    public CarritoResponseDTO findByUser(Long idUsuario) {
        Carrito carrito = carritoRepository.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new GSNotFoundException("Carrito no encontrado"));

        return CarritoMapper.toDTO(carrito);
    }

    @Override
    public CarritoResponseDTO addProduct(Long idPostVenta) {

        String username = SecurityUtils.getUsername();

        Usuario usuario = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new GSNotFoundException("Usuario no encontrado"));

        Carrito carrito = carritoRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new GSNotFoundException("Carrito no encontrado"));

        PostVenta postVenta = postVentaRepository.findById(idPostVenta)
                .orElseThrow(() -> new GSNotFoundException("PostVenta no encontrado"));

        if (productoCarritoRepository.existsByCarritoIdAndPostVentaId(carrito.getId(), idPostVenta)) {
            throw new GSBadRequestException("El producto ya está en el carrito");
        }

        ProductoCarrito pc = ProductoCarrito.builder()
                .carrito(carrito)
                .postVenta(postVenta)
                .build();

        productoCarritoRepository.save(pc);

        carrito.setCoste(carrito.getCoste() + postVenta.getPrecio());

        carritoRepository.save(carrito);

        return CarritoMapper.toDTO(carrito);
    }

    @Override
    public CarritoResponseDTO removeProduct(Long idProductoCarrito) {

        ProductoCarrito pc = productoCarritoRepository.findById(idProductoCarrito)
                .orElseThrow(() -> new GSNotFoundException("ProductoCarrito no encontrado"));

        Carrito carrito = pc.getCarrito();

        Double precio = pc.getPostVenta().getPrecio();

        productoCarritoRepository.delete(pc);

        carrito.setCoste(Math.max(0, carrito.getCoste() - precio));

        carritoRepository.save(carrito);

        return CarritoMapper.toDTO(carrito);
    }

    @Override
    public void delete(Long id) {
        if (!carritoRepository.existsById(id)) {
            throw new GSNotFoundException("Carrito no encontrado");
        }

        carritoRepository.deleteById(id);
    }
}
