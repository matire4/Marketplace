package com.uade.tpo.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entities.Gestor;
import java.util.List;

@Repository
public interface GestorRepository extends JpaRepository<Gestor, Long> {
    List<Gestor> findByCuil(String cuil);
}
