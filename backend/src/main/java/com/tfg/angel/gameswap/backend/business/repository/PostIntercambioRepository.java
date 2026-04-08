package com.tfg.angel.gameswap.backend.business.repository;

import com.tfg.angel.gameswap.backend.business.model.PostIntercambio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostIntercambioRepository extends JpaRepository<PostIntercambio, Long> {

    List<PostIntercambio> findByUsuarioId(Long usuarioId);

    List<PostIntercambio> findByProductoId(Long productoId);

    List<PostIntercambio> findByProductoCambioId(Long productoCambioId);
}
