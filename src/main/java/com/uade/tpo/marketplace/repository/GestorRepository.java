package com.uade.tpo.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entities.Gestor;

@Repository
public interface GestorRepository extends JpaRepository<Gestor, Long> {

}
