package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entities.CarritoHabitacion;
import com.uade.tpo.marketplace.entities.dto.CarritoHabitacionDTO;
import com.uade.tpo.marketplace.exceptions.CarritoNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;

public interface CarritoHabitacionService {
    List<CarritoHabitacionDTO> getCarritosHabitacion();
    
    Optional<CarritoHabitacion> getCarritoHabitacionByUsuario(String username) throws CarritoNotFoundException;
    
    CarritoHabitacion createCarritoHabitacion(
            Long carritoId,
            Long habitacionId,
            String nombreReserva) throws CarritoNotFoundException, HabitacionNotFoundException;
    
    void deleteCarritoHabitacion(Long id) throws CarritoNotFoundException;
    
    List<CarritoHabitacionDTO> getCarritosHabitacionByCarritoId(Long carritoId);
    
    List<CarritoHabitacionDTO> getCarritosHabitacionByHabitacionId(Long habitacionId);
    
    CarritoHabitacionDTO carritoHabitacionToDTO(CarritoHabitacion carritoHabitacion);
}
