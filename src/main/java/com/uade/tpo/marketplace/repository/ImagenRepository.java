package com.uade.tpo.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entities.Imagen;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long> {
    List<Imagen> findByAlojamientoId(Long alojamientoId);
    
    List<Imagen> findByHabitacionId(Long habitacionId);
}