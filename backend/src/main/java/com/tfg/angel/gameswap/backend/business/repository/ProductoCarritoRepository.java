package com.tfg.angel.gameswap.backend.business.repository;

import com.tfg.angel.gameswap.backend.business.model.ProductoCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoCarritoRepository extends JpaRepository<ProductoCarrito, Long> {

    List<ProductoCarrito> findByCarritoId(Long idCarrito);

    boolean existsByCarritoIdAndPostVentaId(Long idCarrito, Long idPostVenta);

    Optional<ProductoCarrito> findByCarritoIdAndPostVentaId(Long carritoId, Long postVentaId);

    void deleteAllByCarritoId(Long carritoId);
}