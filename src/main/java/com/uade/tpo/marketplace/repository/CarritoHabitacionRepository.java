package com.uade.tpo.marketplace.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entities.Carrito;
import com.uade.tpo.marketplace.entities.CarritoHabitacion;
import com.uade.tpo.marketplace.entities.Habitacion;

@Repository
public interface CarritoHabitacionRepository extends JpaRepository<CarritoHabitacion, Long> {
    Optional<CarritoHabitacion> findByCarritoUsuarioUsername(String username);
    List<CarritoHabitacion> findByCarritoId(Long carritoId);
    List<CarritoHabitacion> findByHabitacionId(Long habitacionId);
    void deleteByCarritoAndHabitacion(Carrito carrito, Habitacion habitacion);
} 