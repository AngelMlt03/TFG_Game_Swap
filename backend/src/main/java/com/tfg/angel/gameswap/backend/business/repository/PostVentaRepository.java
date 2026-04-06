package com.tfg.angel.gameswap.backend.business.repository;

import com.tfg.angel.gameswap.backend.business.model.PostVenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostVentaRepository extends JpaRepository<PostVenta, Long> {

    List<PostVenta> findByVendedorId(Long vendedorId);

    List<PostVenta> findByProductoId(Long productoId);
}