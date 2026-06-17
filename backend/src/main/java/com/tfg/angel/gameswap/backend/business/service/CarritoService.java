package com.tfg.angel.gameswap.backend.business.service;

import com.tfg.angel.gameswap.backend.business.dto.response.ProductoCarritoResponseDTO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CarritoService {

    void create();

    void findByUser(Long idUsuario);

    List<ProductoCarritoResponseDTO> getCarrito();
    void agregarProducto(Long idPostVenta);
    void eliminarProducto(Long idPostVenta);

    @Modifying
    @Transactional
    void vaciar();

    boolean estaEnCarrito(Long ventaId);

    void delete(Long id);

    Double getPrecioCarrito();
}
