package com.uade.tpo.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entities.Alojamiento;

@Repository
public interface AlojamientoRepository extends JpaRepository<Alojamiento, Long> {
    List<Alojamiento> findByCiudad(String ciudad);
    List<Alojamiento> findByCategoriaId(Long categoriaId);
    List<Alojamiento> findByGestorId(Long gestorId);
    List<Alojamiento> findByPais(String pais);
}