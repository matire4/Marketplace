package com.uade.tpo.marketplace.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.uade.tpo.marketplace.entities.Cuenta;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long>{
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Cuenta findByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);
}
