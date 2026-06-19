package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.response.ProductoCarritoResponseDTO;
import com.tfg.angel.gameswap.backend.business.mapper.CarritoMapper;
import com.tfg.angel.gameswap.backend.business.mapper.ProductoCarritoMapper;
import com.tfg.angel.gameswap.backend.business.model.*;
import com.tfg.angel.gameswap.backend.business.repository.*;
import com.tfg.angel.gameswap.backend.business.service.CarritoService;
import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;
    private final PostVentaRepository postVentaRepository;
    private final ProductoCarritoRepository productoCarritoRepository;

    private final UsuarioDetailsService usuarioDetailsService;

    @Override
    public void create() {

        Usuario usuario = usuarioDetailsService.obtenerUsuarioActual();

        Carrito carrito = Carrito.builder()
                .usuario(usuario)
                .coste(0.0)
                .build();

        carritoRepository.save(carrito);
    }

    @Override
    public void findByUser(Long idUsuario) {
        Carrito carrito = carritoRepository.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new GSNotFoundException("Carrito no encontrado"));

        CarritoMapper.toDTO(carrito);
    }

    @Override
    public void delete(Long id) {
        if (!carritoRepository.existsById(id)) {
            throw new GSNotFoundException("Carrito no encontrado");
        }

        carritoRepository.deleteById(id);
    }

    @Override
    public Double getPrecioCarrito() {
        Usuario usuario = usuarioDetailsService.obtenerUsuarioActual();

        Carrito carrito = carritoRepository
                .findByUsuarioId(usuario.getId())
                .orElseThrow();

        return carrito.getCoste();
    }

    public List<ProductoCarritoResponseDTO> getCarrito() {

        Usuario usuario = usuarioDetailsService.obtenerUsuarioActual();

        Carrito c = carritoRepository
                        .findByUsuarioId(usuario.getId())
                        .orElseThrow();

        Double costeTotal = c.getProductos().stream()
                .mapToDouble(p -> p.getPostVenta().getPrecio())
                .sum();

        c.setCoste(costeTotal);

        Carrito carrito = carritoRepository.save(c);

        return productoCarritoRepository
                .findByCarritoId(carrito.getId())
                .stream()
                .map(ProductoCarritoMapper::toDTO)
                .toList();
    }

    @Override
    public void agregarProducto(Long idPostVenta) {

        Usuario usuario = usuarioDetailsService.obtenerUsuarioActual();

        if ( carritoRepository.findByUsuarioId(usuario.getId()).isEmpty() ) {
            this.create();
        }

        Carrito carrito = carritoRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new GSNotFoundException("Carrito no encontrado"));

        PostVenta postVenta = postVentaRepository.findById(idPostVenta)
                .orElseThrow(() -> new GSNotFoundException("PostVenta no encontrado"));

        if (productoCarritoRepository.existsByCarritoIdAndPostVentaId(carrito.getId(), idPostVenta)) {
            throw new GSBadRequestException("El producto ya está en el carrito");
        }

        if (productoCarritoRepository.findByCarritoIdAndPostVentaId(carrito.getId(),idPostVenta)
            .isPresent()) return;

        carrito.setCoste(carrito.getCoste() + postVenta.getPrecio());

        ProductoCarrito pc = ProductoCarrito.builder()
                        .carrito(carrito)
                        .postVenta(postVenta)
                        .build();

        productoCarritoRepository.save(pc);
        carritoRepository.save(carrito);
    }

    @Override
    public void eliminarProducto(Long idPostVenta) {

        Usuario usuario = usuarioDetailsService.obtenerUsuarioActual();

        Carrito carrito = carritoRepository
                        .findByUsuarioId(usuario.getId())
                        .orElseThrow();

        ProductoCarrito pc = productoCarritoRepository.findByCarritoIdAndPostVentaId(carrito.getId(), idPostVenta)
                .orElseThrow(() -> new GSNotFoundException("ProductoCarrito no encontrado"));

        Double precio = pc.getPostVenta().getPrecio();
        Double precioActual = carrito.getCoste() != null? carrito.getCoste() : 0.0;
        carrito.setCoste(Math.max(0, precioActual - precio));

        carritoRepository.save(carrito);

        productoCarritoRepository.delete(pc);
    }

    @Transactional
    @Override
    public void vaciar() {

        Usuario usuario = usuarioDetailsService.obtenerUsuarioActual();

        Carrito carrito = carritoRepository
                        .findByUsuarioId(usuario.getId())
                        .orElseThrow();
        
        productoCarritoRepository.deleteAllByCarritoId(carrito.getId());

        carrito.setCoste(0.0);
        carritoRepository.save(carrito);
    }

    @Override
    public boolean estaEnCarrito(Long ventaId) {

        Usuario usuario = usuarioDetailsService.obtenerUsuarioActual();

        return productoCarritoRepository.existsByCarritoIdAndPostVentaId(
                usuario.getId(),
                ventaId
        );
    }
}
