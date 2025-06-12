package com.uade.tpo.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Usuario findByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    Usuario findByCarritoId(Long carritoId);
}