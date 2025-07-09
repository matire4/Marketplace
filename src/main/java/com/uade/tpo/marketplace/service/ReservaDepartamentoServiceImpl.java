package com.uade.tpo.marketplace.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entities.Departamento;
import com.uade.tpo.marketplace.entities.Reserva;
import com.uade.tpo.marketplace.entities.ReservaDepartamento;
import com.uade.tpo.marketplace.entities.dto.ReservaDepartamentoDTO;
import com.uade.tpo.marketplace.exceptions.FechaYaReservadaException;
import com.uade.tpo.marketplace.exceptions.ReservaNotFounException;
import com.uade.tpo.marketplace.repository.DepartamentoRepository;
import com.uade.tpo.marketplace.repository.ReservaDepartamentoRepository;
import com.uade.tpo.marketplace.repository.ReservaRepository;

@Service
public class ReservaDepartamentoServiceImpl implements ReservaDepartamentoService {

    @Autowired
    private ReservaDepartamentoRepository reservaDepartamentoRepository;
    
    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ReservaDepartamentoDTO> getReservasDepartamentosByReservaId(Long reservaId) throws ReservaNotFounException {
        Optional<Reserva> reserva = reservaRepository.findById(reservaId);
        if (!reserva.isPresent()) {
            throw new ReservaNotFounException();
        }
        
        List<ReservaDepartamento> reservasDepartamento = reservaDepartamentoRepository.findByReservaId(reservaId);
        return reservasDepartamento.stream()
                .map(this::reservaDepartamentoToReservaDepartamentoDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReservaDepartamentoDTO createReservaDepartamento(ReservaDepartamentoDTO reservaDepartamentoDTO) 
            throws ReservaNotFounException {
        
        Optional<Reserva> reserva = reservaRepository.findById(reservaDepartamentoDTO.getReservaId());
        if (!reserva.isPresent()) {
            throw new ReservaNotFounException();
        }
        
        Optional<Departamento> departamento = departamentoRepository.findById(reservaDepartamentoDTO.getDepartamentoId());
        if (!departamento.isPresent()) {
            throw new ReservaNotFounException();
        }
        
        ReservaDepartamento reservaDepartamento = new ReservaDepartamento();
        reservaDepartamento.setTitularReserva(reservaDepartamentoDTO.getTitularReserva());
        reservaDepartamento.setNombreReserva(reservaDepartamentoDTO.getNombreReserva());
        reservaDepartamento.setReserva(reserva.get());
        reservaDepartamento.setDepartamento(departamento.get());
        reservaDepartamento.setFechaDesde(reservaDepartamentoDTO.getFechaDesde());
        reservaDepartamento.setFechaHasta(reservaDepartamentoDTO.getFechaHasta());
        reservaDepartamento.setCantidadPersonas(reservaDepartamentoDTO.getCantidadPersonas());
        reservaDepartamento.setPrecio(reservaDepartamentoDTO.getPrecio());
        reservaDepartamento.setEstado(reservaDepartamentoDTO.getEstado());
        
        ReservaDepartamento saved = reservaDepartamentoRepository.save(reservaDepartamento);
        return reservaDepartamentoToReservaDepartamentoDTO(saved);
    }

    @Override
    @Transactional
    public ReservaDepartamentoDTO updateReservaDepartamento(Long reservaDepartamentoId, 
            ReservaDepartamentoDTO reservaDepartamentoDTO) throws ReservaNotFounException {
        
        Optional<ReservaDepartamento> existing = reservaDepartamentoRepository.findById(reservaDepartamentoId);
        if (!existing.isPresent()) {
            throw new ReservaNotFounException();
        }
        
        ReservaDepartamento reservaDepartamento = existing.get();
        reservaDepartamento.setNombreReserva(reservaDepartamentoDTO.getNombreReserva());
        reservaDepartamento.setFechaDesde(reservaDepartamentoDTO.getFechaDesde());
        reservaDepartamento.setFechaHasta(reservaDepartamentoDTO.getFechaHasta());
        reservaDepartamento.setCantidadPersonas(reservaDepartamentoDTO.getCantidadPersonas());
        reservaDepartamento.setPrecio(reservaDepartamentoDTO.getPrecio());
        reservaDepartamento.setEstado(reservaDepartamentoDTO.getEstado());
        
        ReservaDepartamento saved = reservaDepartamentoRepository.save(reservaDepartamento);
        return reservaDepartamentoToReservaDepartamentoDTO(saved);
    }

    @Override
    @Transactional
    public void deleteReservaDepartamento(Long reservaDepartamentoId) throws ReservaNotFounException {
        Optional<ReservaDepartamento> reservaDepartamento = reservaDepartamentoRepository.findById(reservaDepartamentoId);
        if (!reservaDepartamento.isPresent()) {
            throw new ReservaNotFounException();
        }
        
        reservaDepartamentoRepository.deleteById(reservaDepartamentoId);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaDepartamentoDTO reservaDepartamentoToReservaDepartamentoDTO(ReservaDepartamento reservaDepartamento) {
        return ReservaDepartamentoDTO.builder()
                .id(reservaDepartamento.getId())
                .titularReserva(reservaDepartamento.getTitularReserva())
                .nombreReserva(reservaDepartamento.getNombreReserva())
                .reservaId(reservaDepartamento.getReserva().getId())
                .departamentoId(reservaDepartamento.getDepartamento().getId())
                .departamentoNombre(reservaDepartamento.getDepartamento().getNumeroDepartamento())
                .fechaDesde(reservaDepartamento.getFechaDesde())
                .fechaHasta(reservaDepartamento.getFechaHasta())
                .cantidadPersonas(reservaDepartamento.getCantidadPersonas())
                .precio(reservaDepartamento.getPrecio())
                .estado(reservaDepartamento.getEstado())
                .build();
    }

    @Override
    public void checkFechasReservadas(Long departamentoId, String checkIn, String checkOut)
            throws FechaYaReservadaException {
        Date checkInDate = Date.from(java.time.LocalDate.parse(checkIn).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
        Date checkOutDate = Date.from(java.time.LocalDate.parse(checkOut).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
        List<ReservaDepartamento> reservas = reservaDepartamentoRepository.findByDepartamentoId(departamentoId);
        for (ReservaDepartamento reserva : reservas) {
            Date fechaDesde = new Date(reserva.getFechaDesde().getTime());
            Date fechaHasta = new Date(reserva.getFechaHasta().getTime());
            if (fechaDesde.compareTo(checkOutDate) < 0 && fechaHasta.compareTo(checkInDate) > 0) {
                throw new FechaYaReservadaException();
            }
        }
    }

    @Override
    public List<ReservaDepartamento> findByGestorId(Long id) {
        return reservaDepartamentoRepository.findByGestorId(id);
    }
}