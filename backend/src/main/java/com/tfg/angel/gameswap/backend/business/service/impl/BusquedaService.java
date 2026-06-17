package com.tfg.angel.gameswap.backend.business.service.impl;

import com.tfg.angel.gameswap.backend.business.dto.response.*;
import com.tfg.angel.gameswap.backend.business.model.Usuario;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import com.tfg.angel.gameswap.backend.business.service.PostIntercambioService;
import com.tfg.angel.gameswap.backend.business.service.PostVentaService;
import com.tfg.angel.gameswap.backend.business.service.ProductoService;
import com.tfg.angel.gameswap.backend.security.UsuarioDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusquedaService {

    private final PostVentaService postVentaService;
    private final PostIntercambioService postIntercambioService;
    private final ProductoService productoService;
    private final UsuarioDetailsService usuarioDetailsService;

    public List<PostBusquedaDTO> buscar(String nombre, String tipo, String plataforma, String estado) {

        List<PostBusquedaDTO> resultado = new ArrayList<>();

        Usuario usuarioActual = usuarioDetailsService.obtenerUsuarioActual();

        if (tipo == null || tipo.equalsIgnoreCase("VENTA")) {

            postVentaService.findByEstado(EstadoPost.ACTIVO)
                    .stream()
                    .filter(p -> !p.getIdVendedor().equals(usuarioActual.getId()))
                    .filter(p -> nombre == null ||
                            p.getNombreProducto().toLowerCase().contains(nombre.toLowerCase()))
                    .filter(p -> plataforma == null ||
                            p.getPlataforma().toLowerCase().contains(plataforma.toLowerCase()))
                    .filter(p -> estado == null ||
                            productoService.findById(p.getIdProducto()).getEstado().toString().toLowerCase().contains(estado.toLowerCase()))
                    .forEach(p -> {
                        ProductoResponseDTO producto = productoService.findById(p.getIdProducto());
                        resultado.add(getPostVenta(p, producto));});
        }

        if (tipo == null || tipo.equalsIgnoreCase("INTERCAMBIO")) {

            postIntercambioService.findByEstado(EstadoPost.ACTIVO)
                    .stream()
                    .filter(p -> !p.getIdUsuario().equals(usuarioActual.getId()))
                    .filter(p -> nombre == null ||
                            p.getNombreProducto().toLowerCase().contains(nombre.toLowerCase()))
                    .filter(p -> plataforma == null ||
                            p.getPlataforma().toLowerCase().contains(plataforma.toLowerCase()))
                    .filter(p -> estado == null || productoService
                                    .findById(p.getIdProducto())
                                    .getEstado()
                                    .name()
                                    .equalsIgnoreCase(estado))
                    .forEach(p -> {
                        ProductoResponseDTO producto = productoService.findById(p.getIdProducto());
                        ProductoResponseDTO productoIntercambio = productoService.findById(p.getIdProductoIntercambio());
                        resultado.add(getIntercambio(p, producto, productoIntercambio));}
                    );
        }

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
                .nombreProductoIntercambio(p.getNombreProductoIntercambio())
                .plataformaIntercambio(p.getPlataformaIntercambio())
                .estadoIntercambio(String.valueOf(productoIntercambio.getEstado()))

                .nombreUsuario(p.getNombreUsuario())
                .descripcion(p.getDescripcion())

                .build();
    }
}