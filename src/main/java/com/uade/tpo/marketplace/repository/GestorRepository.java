package com.uade.tpo.marketplace.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entities.Gestor;

import io.jsonwebtoken.security.Jwks.OP;

@Repository
public interface GestorRepository extends JpaRepository<Gestor, Long> {
    List<Gestor> findByCuil(String cuil);

    Optional<Gestor> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameAndIdNot(String username, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);
}
