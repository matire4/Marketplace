package com.uade.tpo.marketplace.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entities.CarritoHabitacion;

@Repository
public interface CarritoHabitacionRepository extends JpaRepository<CarritoHabitacion, Long> {
    Optional<CarritoHabitacion> findByCarritoUsuarioUsername(String username);
    List<CarritoHabitacion> findByCarritoId(Long carritoId);
    List<CarritoHabitacion> findByHabitacionId(Long habitacionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CarritoHabitacion ch WHERE ch.carrito.id = :id AND ch.habitacion.id = :id2")
    void deleteByCarritoIdAndHabitacionId(@Param("id") Long id, @Param("id2") Long id2);
}