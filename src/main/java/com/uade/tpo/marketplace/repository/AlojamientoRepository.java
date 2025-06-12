package com.uade.tpo.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entities.Alojamiento;
import com.uade.tpo.marketplace.entities.Gestor;

@Repository
public interface AlojamientoRepository extends JpaRepository<Alojamiento, Long> {
    List<Alojamiento> findByCiudad(String ciudad);
    List<Alojamiento> findByCategoriaId(Long categoriaId);
    List<Alojamiento> findByGestor(Gestor gestor);
    List<Alojamiento> findByPais(String pais);
}