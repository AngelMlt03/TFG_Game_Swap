package com.tfg.angel.gameswap.backend.business.repository;

import com.tfg.angel.gameswap.backend.business.model.PostIntercambio;
import com.tfg.angel.gameswap.backend.business.model.enums.EstadoPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostIntercambioRepository extends JpaRepository<PostIntercambio, Long> {

    List<PostIntercambio> findByUsuarioId(Long usuarioId);

    List<PostIntercambio> findByProductoId(Long productoId);

    List<PostIntercambio> findByProductoCambioId(Long productoCambioId);

    List<PostIntercambio> findByEstado(EstadoPost estado);

    List<PostIntercambio> findByUsuarioIdAndEstado(Long id, EstadoPost estadoPost);

    List<PostIntercambio> findTop8ByEstadoOrderByIdDesc(EstadoPost estadoPost);

    List<PostIntercambio> findTop4ByEstadoOrderByIdDesc(EstadoPost estadoPost);

    @Query("""
    SELECT COUNT(p) > 0
    FROM PostIntercambio p
    WHERE p.estado = 'ACTIVO'
    AND LOWER(p.producto.nombre) = LOWER(:juegoBuscado)
    AND LOWER(p.productoCambio.nombre) = LOWER(:tuJuego)
    """)
    boolean existeIntercambioInverso(@Param("tuJuego") String tuJuego, @Param("juegoBuscado") String juegoBuscado);
}
