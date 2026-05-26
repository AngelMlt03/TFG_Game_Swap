package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.model.*;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import com.tfg.angel.gameswap.backend.business.repository.*;
import com.tfg.angel.gameswap.backend.business.service.CarritoService;
import com.tfg.angel.gameswap.backend.exception.GSBadRequestException;
import com.tfg.angel.gameswap.backend.exception.GSNotFoundException;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransaccionService {

    private final PostVentaRepository postVentaRepository;
    private final PostIntercambioRepository postIntercambioRepository;
    private final GuardadoRepository guardadoRepository;
    private final CompraVentaRepository compraVentaRepository;
    private final IntercambioRepository intercambioRepository;
    private final ProductoCarritoRepository productoCarritoRepository;
    private final CarritoRepository carritoRepository;

    private final UsuarioDetailsService usuarioDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final CarritoService carritoService;

    public Double comprar(Long idPostVenta) {

        PostVenta postVenta = postVentaRepository.findById(idPostVenta)
                .orElseThrow(() -> new GSNotFoundException("PostVenta no encontrado"));

        Usuario usuarioActual = usuarioDetailsService.obtenerUsuarioActual();

        Usuario vendedor = postVenta.getVendedor();

        if (usuarioActual.getId().equals(vendedor.getId())) {
            throw new GSBadRequestException("No puedes comprar tu propio producto");
        }

        if (postVenta.getEstado().equals(EstadoPost.FINALIZADO)) {
            throw new GSBadRequestException("El producto ya está vendido");
        }

        if (usuarioActual.getSaldo() < postVenta.getPrecio()) {
            throw new GSBadRequestException("Saldo insuficiente");
        }

        usuarioActual.setSaldo(usuarioActual.getSaldo() - postVenta.getPrecio());
        vendedor.setSaldo(vendedor.getSaldo() + postVenta.getPrecio());

        usuarioRepository.save(vendedor);
        usuarioRepository.save(usuarioActual);

        postVenta.setEstado(EstadoPost.FINALIZADO);
        postVentaRepository.save(postVenta);

        Optional<Guardado> guardado = guardadoRepository.findByIdUsuarioAndIdPost(usuarioActual.getId(), postVenta.getId());
        guardado.ifPresent(value -> guardadoRepository.deleteById(value.getId()));

        CompraVenta entity = CompraVenta.builder()
                .postVenta(postVenta)
                .comprador(usuarioActual)
                .precio(postVenta.getPrecio())
                .fecha(LocalDate.now())
                .build();

        compraVentaRepository.save(entity);

        Carrito carrito = carritoRepository.findByUsuarioId(usuarioActual.getId())
                .orElseThrow(() -> new GSNotFoundException("Carrito no encontrado"));

        if (productoCarritoRepository.existsByCarritoIdAndPostVentaId(carrito.getId(), idPostVenta)) {
            carritoService.eliminarProducto(idPostVenta);
        }

        return usuarioActual.getSaldo();
    }

    public void intercambiar(Long idPostIntercambio) {

        PostIntercambio post = postIntercambioRepository.findById(idPostIntercambio)
                .orElseThrow(() -> new GSNotFoundException("PostIntercambio no encontrado"));

        Usuario usuarioActual = usuarioDetailsService.obtenerUsuarioActual();

        if (post.getUsuario().getId().equals(usuarioActual.getId())) {
            throw new GSBadRequestException("No puedes intercambiar contigo mismo");
        }

        post.setEstado(EstadoPost.FINALIZADO);
        postIntercambioRepository.save(post);

        Intercambio intercambio = Intercambio.builder()
                .postIntercambio(post)
                .usuarioCambio(usuarioActual)
                .fecha(LocalDate.now())
                .build();

        intercambioRepository.save(intercambio);
    }
}
