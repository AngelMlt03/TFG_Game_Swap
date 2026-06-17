package com.tfg.angel.gameswap.backend.business.repository;

import com.tfg.angel.gameswap.backend.business.model.Guardado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuardadoRepository extends JpaRepository<Guardado, Long> {

    List<Guardado> findByIdUsuarioAndTipoPost(Long idUsuario, String tipoPost);

    Optional<Guardado> findByIdUsuarioAndIdPost(Long idUsuario, Long idPost);

    boolean existsByIdUsuarioAndIdPostAndTipoPost(Long usuarioId, Long idPost, String tipoPost);

    Optional<Guardado> findByIdUsuarioAndIdPostAndTipoPost(Long usuarioId, Long idPost, String tipoPost);
}
