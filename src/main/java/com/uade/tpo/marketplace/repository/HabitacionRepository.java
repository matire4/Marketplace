package com.uade.tpo.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entities.Habitacion;
import com.uade.tpo.marketplace.enums.TipoHabitacion;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> { 
    List<Habitacion> findByTipoHabitacion(TipoHabitacion tipoHabitacion);
    List<Habitacion> findByPrecioMin(int precioMin);
    List<Habitacion> findByPrecioMax(int precioMax);
    //Deberia haber uno solo con Precio por noche, pero prefiero separarlo para el front
    List<Habitacion> findByAmbientes(int ambientes);
    List<Habitacion> findByDormitorios(int dormitorios);
    List<Habitacion> findByCapacidad(int capacidad);
    List<Habitacion> findByReservasHabitacion(int reservasHabitacion);
}