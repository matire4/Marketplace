package com.uade.tpo.marketplace.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entities.Habitacion;
import com.uade.tpo.marketplace.entities.Reserva;
import com.uade.tpo.marketplace.entities.ReservaHabitacion;
import com.uade.tpo.marketplace.entities.dto.ReservaHabitacionDTO;
import com.uade.tpo.marketplace.exceptions.FechaYaReservadaException;
import com.uade.tpo.marketplace.exceptions.ReservaNotFounException;
import com.uade.tpo.marketplace.repository.HabitacionRepository;
import com.uade.tpo.marketplace.repository.ReservaHabitacionRepository;
import com.uade.tpo.marketplace.repository.ReservaRepository;

@Service
public class ReservaHabitacionServiceImpl implements ReservaHabitacionService {

    @Autowired
    private ReservaHabitacionRepository reservaHabitacionRepository;
    
    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private HabitacionRepository habitacionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ReservaHabitacionDTO> getReservasHabitacionByReservaId(Long reservaId) throws ReservaNotFounException {
        Optional<Reserva> reserva = reservaRepository.findById(reservaId);
        if (!reserva.isPresent()) {
            throw new ReservaNotFounException();
        }
        
        List<ReservaHabitacion> reservasHabitacion = reservaHabitacionRepository.findByReservaId(reservaId);
        return reservasHabitacion.stream()
                .map(this::reservaHabitacionToReservaHabitacionDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReservaHabitacionDTO createReservaHabitacion(ReservaHabitacionDTO reservaHabitacionDTO) 
            throws ReservaNotFounException {
        
        Optional<Reserva> reserva = reservaRepository.findById(reservaHabitacionDTO.getReservaId());
        if (!reserva.isPresent()) {
            throw new ReservaNotFounException();
        }
        
        Optional<Habitacion> habitacion = habitacionRepository.findById(reservaHabitacionDTO.getHabitacionId());
        if (!habitacion.isPresent()) {
            throw new ReservaNotFounException();
        }
        
        ReservaHabitacion reservaHabitacion = new ReservaHabitacion();
        reservaHabitacion.setTitularReserva(reservaHabitacionDTO.getTitularReserva());
        reservaHabitacion.setNombreReserva(reservaHabitacionDTO.getNombreReserva());
        reservaHabitacion.setReserva(reserva.get());
        reservaHabitacion.setHabitacion(habitacion.get());
        reservaHabitacion.setFechaDesde(reservaHabitacionDTO.getFechaDesde());
        reservaHabitacion.setFechaHasta(reservaHabitacionDTO.getFechaHasta());
        reservaHabitacion.setCantidadPersonas(reservaHabitacionDTO.getCantidadPersonas());
        reservaHabitacion.setPrecio(reservaHabitacionDTO.getPrecio());
        reservaHabitacion.setEstado(reservaHabitacionDTO.getEstado());
        
        ReservaHabitacion saved = reservaHabitacionRepository.save(reservaHabitacion);
        return reservaHabitacionToReservaHabitacionDTO(saved);
    }

    @Override
    @Transactional
    public ReservaHabitacionDTO updateReservaHabitacion(Long reservaHabitacionId, 
            ReservaHabitacionDTO reservaHabitacionDTO) throws ReservaNotFounException {
        
        Optional<ReservaHabitacion> existing = reservaHabitacionRepository.findById(reservaHabitacionId);
        if (!existing.isPresent()) {
            throw new ReservaNotFounException();
        }
        
        ReservaHabitacion reservaHabitacion = existing.get();
        reservaHabitacion.setNombreReserva(reservaHabitacionDTO.getNombreReserva());
        reservaHabitacion.setFechaDesde(reservaHabitacionDTO.getFechaDesde());
        reservaHabitacion.setFechaHasta(reservaHabitacionDTO.getFechaHasta());
        reservaHabitacion.setCantidadPersonas(reservaHabitacionDTO.getCantidadPersonas());
        reservaHabitacion.setPrecio(reservaHabitacionDTO.getPrecio());
        reservaHabitacion.setEstado(reservaHabitacionDTO.getEstado());
        
        ReservaHabitacion saved = reservaHabitacionRepository.save(reservaHabitacion);
        return reservaHabitacionToReservaHabitacionDTO(saved);
    }

    @Override
    @Transactional
    public void deleteReservaHabitacion(Long reservaHabitacionId) throws ReservaNotFounException {
        Optional<ReservaHabitacion> reservaHabitacion = reservaHabitacionRepository.findById(reservaHabitacionId);
        if (!reservaHabitacion.isPresent()) {
            throw new ReservaNotFounException();
        }
        
        reservaHabitacionRepository.deleteById(reservaHabitacionId);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaHabitacionDTO reservaHabitacionToReservaHabitacionDTO(ReservaHabitacion reservaHabitacion) {
        return ReservaHabitacionDTO.builder()
                .id(reservaHabitacion.getId())
                .titularReserva(reservaHabitacion.getTitularReserva())
                .nombreReserva(reservaHabitacion.getNombreReserva())
                .reservaId(reservaHabitacion.getReserva().getId())
                .habitacionId(reservaHabitacion.getHabitacion().getId())
                .habitacionNombre(reservaHabitacion.getHabitacion().getNumeroHabitacion())
                .fechaDesde(reservaHabitacion.getFechaDesde())
                .fechaHasta(reservaHabitacion.getFechaHasta())
                .cantidadPersonas(reservaHabitacion.getCantidadPersonas())
                .precio(reservaHabitacion.getPrecio())
                .estado(reservaHabitacion.getEstado())
                .hotelId(reservaHabitacion.getHabitacion().getHotel().getId().toString())
                .build();
    }

    @Override
    public void checkFechasReservadas(Long habitacionId, String checkIn, String checkOut)
            throws FechaYaReservadaException {
        Date checkInDate = Date.from(java.time.LocalDate.parse(checkIn).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
        Date checkOutDate = Date.from(java.time.LocalDate.parse(checkOut).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
        List<ReservaHabitacion> reservas = reservaHabitacionRepository.findByHabitacionId(habitacionId);
        for (ReservaHabitacion reserva : reservas) {
            Date fechaDesde = new Date(reserva.getFechaDesde().getTime());
            Date fechaHasta = new Date(reserva.getFechaHasta().getTime());
            if (fechaDesde.compareTo(checkOutDate) < 0 && fechaHasta.compareTo(checkInDate) > 0) {
                throw new FechaYaReservadaException();
            }
        }
    }

    @Override
    public List<ReservaHabitacion> findByGestorId(Long id) {
        return reservaHabitacionRepository.findByGestorId(id);
    }
}
