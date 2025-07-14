package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entities.ReservaHabitacion;
import com.uade.tpo.marketplace.entities.dto.ReservaHabitacionDTO;
import com.uade.tpo.marketplace.exceptions.FechaYaReservadaException;
import com.uade.tpo.marketplace.exceptions.ReservaNotFounException;

public interface ReservaHabitacionService {
    List<ReservaHabitacionDTO> getReservasHabitacionByReservaId(Long reservaId) throws ReservaNotFounException;
    ReservaHabitacionDTO createReservaHabitacion(ReservaHabitacionDTO reservaHabitacionDTO) throws ReservaNotFounException;
    ReservaHabitacionDTO updateReservaHabitacion(Long reservaHabitacionId, ReservaHabitacionDTO reservaHabitacionDTO) throws ReservaNotFounException;
    void deleteReservaHabitacion(Long reservaHabitacionId) throws ReservaNotFounException;
    ReservaHabitacionDTO reservaHabitacionToReservaHabitacionDTO(ReservaHabitacion reservaHabitacion);
    void checkFechasReservadas(Long habitacionId, String checkIn, String checkOut) throws FechaYaReservadaException;
    List<ReservaHabitacion> findByGestorId(Long id);
    Optional<ReservaHabitacion> getReservaHabitacionById(Long itemId);
    void save(ReservaHabitacion rh);
}
