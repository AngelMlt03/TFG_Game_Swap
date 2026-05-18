package com.tfg.angel.gameswap.backend.business.repository;

import com.tfg.angel.gameswap.backend.business.model.Intercambio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IntercambioRepository extends JpaRepository<Intercambio, Long> {

    List<Intercambio> findByPostIntercambioUsuarioId(Long idUsuario);

    List<Intercambio> findByUsuarioCambioId(Long idUsuario);

}
