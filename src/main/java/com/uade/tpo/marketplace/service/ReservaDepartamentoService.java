package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entities.ReservaDepartamento;
import com.uade.tpo.marketplace.entities.dto.ReservaDepartamentoDTO;
import com.uade.tpo.marketplace.exceptions.DepartamentoNotFoundException;
import com.uade.tpo.marketplace.exceptions.FechaYaReservadaException;
import com.uade.tpo.marketplace.exceptions.ReservaNotFounException;

public interface ReservaDepartamentoService {
    List<ReservaDepartamentoDTO> getReservasDepartamentosByReservaId(Long reservaId) throws ReservaNotFounException;
    ReservaDepartamentoDTO createReservaDepartamento(ReservaDepartamentoDTO reservaDepartamentoDTO) throws ReservaNotFounException, DepartamentoNotFoundException;
    ReservaDepartamentoDTO updateReservaDepartamento(Long reservaDepartamentoId, ReservaDepartamentoDTO reservaDepartamentoDTO) throws ReservaNotFounException;
    void deleteReservaDepartamento(Long reservaDepartamentoId) throws ReservaNotFounException;
    ReservaDepartamentoDTO reservaDepartamentoToReservaDepartamentoDTO(ReservaDepartamento reservaDepartamento);
    void checkFechasReservadas(Long departamentoId, String checkIn, String checkOut) throws FechaYaReservadaException;
    List<ReservaDepartamento> findByGestorId(Long id);
    Optional<ReservaDepartamento> getReservaDepartamentoById(Long itemId);
    void save(ReservaDepartamento rd);
}