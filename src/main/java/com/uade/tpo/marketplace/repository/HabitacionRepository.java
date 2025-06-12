package com.uade.tpo.marketplace.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entities.Habitacion;
import com.uade.tpo.marketplace.entities.Hotel;
import com.uade.tpo.marketplace.enums.TipoHabitacion;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> { 
    List<Habitacion> findByTipoHabitacion(TipoHabitacion tipoHabitacion);
    Optional<Habitacion> findByHotelAndNumeroHabitacion(Hotel hotel, String numeroHabitacion);
    List<Habitacion> findByHotel(Hotel hotel);
}