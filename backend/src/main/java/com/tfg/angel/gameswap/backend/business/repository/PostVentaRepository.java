package com.tfg.angel.gameswap.backend.business.repository;

import com.tfg.angel.gameswap.backend.business.model.PostVenta;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostVentaRepository extends JpaRepository<PostVenta, Long> {

    List<PostVenta> findByVendedorId(Long vendedorId);

    List<PostVenta> findByProductoId(Long productoId);

    List<PostVenta> findByEstado(EstadoPost estado);

    List<PostVenta> findByVendedorIdAndEstado(Long idUsuario, EstadoPost estadoPost);

    List<PostVenta> findTop8ByEstadoOrderByIdDesc(EstadoPost estadoPost);

    List<PostVenta> findTop4ByEstadoOrderByIdDesc(EstadoPost estadoPost);
}