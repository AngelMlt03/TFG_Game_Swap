package com.tfg.angel.gameswap.backend.business.repository;

import com.tfg.angel.gameswap.backend.business.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByNUsuario(String nUsuario);

    boolean existsByCorreo(String correo);

    boolean existsByNUsuario(String nUsuario);
}
