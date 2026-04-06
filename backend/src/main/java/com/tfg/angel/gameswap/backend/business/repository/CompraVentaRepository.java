package com.tfg.angel.gameswap.backend.business.repository;

import com.tfg.angel.gameswap.backend.business.model.CompraVenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompraVentaRepository extends JpaRepository<CompraVenta, Long> {

    List<CompraVenta> findByCompradorId(Long idComprador);

    List<CompraVenta> findByVendedorId(Long idVendedor);

    List<CompraVenta> findByProductoId(Long idProducto);
}
