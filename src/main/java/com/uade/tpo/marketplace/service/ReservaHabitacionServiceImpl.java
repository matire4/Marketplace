package com.uade.tpo.marketplace.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.uade.tpo.marketplace.entities.ReservaHabitacion;
import com.uade.tpo.marketplace.entities.dto.ReservaHabitacionDTO;
import com.uade.tpo.marketplace.exceptions.ReservaNotFounException;
import com.uade.tpo.marketplace.repository.ReservaHabitacionRepository;

public class ReservaHabitacionServiceImpl implements ReservaHabitacionService {
    @Autowired
    private ReservaHabitacionRepository reservaHabitacionRepository;

    @Override
    public List<ReservaHabitacionDTO> getReservasHabitacionesByReservaId(Long reservaId) throws ReservaNotFounException {
        List<ReservaHabitacion> reservasHabitaciones = reservaHabitacionRepository.findByReservaId(reservaId);
        return reservasHabitaciones.stream()
                .map(this::reservaHabitacionToReservaHabitacionDTO)
                .toList();
    }
    @Override
    public ReservaHabitacionDTO createReservaHabitacion(ReservaHabitacionDTO reservaHabitacionDTO) throws ReservaNotFounException {
        ReservaHabitacion reservaHabitacion = new ReservaHabitacion();
        reservaHabitacion.setNombreReserva(reservaHabitacionDTO.getNombreReserva());
        reservaHabitacion.setFechaDesde(reservaHabitacionDTO.getFechaDesde());
        reservaHabitacion.setFechaHasta(reservaHabitacionDTO.getFechaHasta());
        reservaHabitacion.setEstado(reservaHabitacionDTO.getEstado());
        
        ReservaHabitacion savedReservaHabitacion = reservaHabitacionRepository.save(reservaHabitacion);
        return reservaHabitacionToReservaHabitacionDTO(savedReservaHabitacion);
    }
    @Override
    public ReservaHabitacionDTO updateReservaHabitacion(Long reservaHabitacionId, ReservaHabitacionDTO reservaHabitacionDTO) throws ReservaNotFounException {
        ReservaHabitacion reservaHabitacion = reservaHabitacionRepository.findById(reservaHabitacionId)
                .orElseThrow(() -> new ReservaNotFounException());
        
        reservaHabitacion.setNombreReserva(reservaHabitacionDTO.getNombreReserva());
        reservaHabitacion.setFechaDesde(reservaHabitacionDTO.getFechaDesde());
        reservaHabitacion.setFechaHasta(reservaHabitacionDTO.getFechaHasta());
        reservaHabitacion.setEstado(reservaHabitacionDTO.getEstado());
        
        ReservaHabitacion updatedReservaHabitacion = reservaHabitacionRepository.save(reservaHabitacion);
        return reservaHabitacionToReservaHabitacionDTO(updatedReservaHabitacion);
    }
    @Override
    public void deleteReservaHabitacion(Long reservaHabitacionId) throws ReservaNotFounException {
        ReservaHabitacion reservaHabitacion = reservaHabitacionRepository.findById(reservaHabitacionId)
                .orElseThrow(() -> new ReservaNotFounException());
        reservaHabitacionRepository.delete(reservaHabitacion);
    }
    @Override
    public ReservaHabitacionDTO reservaHabitacionToReservaHabitacionDTO(ReservaHabitacion reservaHabitacion) {
        ReservaHabitacionDTO reservaHabitacionDTO = new ReservaHabitacionDTO();
        reservaHabitacionDTO.setNombreReserva(reservaHabitacion.getNombreReserva());
        reservaHabitacionDTO.setReservaId(reservaHabitacion.getReserva().getId());
        reservaHabitacionDTO.setHabitacionId(reservaHabitacion.getHabitacion().getId());
        reservaHabitacionDTO.setEstado(reservaHabitacion.getEstado());
        return reservaHabitacionDTO;
    }
}
