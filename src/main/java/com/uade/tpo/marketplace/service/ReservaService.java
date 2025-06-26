package com.uade.tpo.marketplace.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entities.Reserva;
import com.uade.tpo.marketplace.entities.dto.ReservaDTO;
import com.uade.tpo.marketplace.entities.dto.ReservaHabitacionDTO;
import com.uade.tpo.marketplace.entities.dto.UsuarioDTO;
import com.uade.tpo.marketplace.exceptions.CarritoEmptyException;
import com.uade.tpo.marketplace.exceptions.CarritoNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.exceptions.ReservaNotFounException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;

public interface ReservaService {
    List<ReservaDTO> getReservas();

    List<ReservaDTO> getReservasByUsuario(String usuario) throws ReservaNotFounException, UsuarioNotFoundException;

    Optional<Reserva> getReservaById(Long reservaId) throws ReservaNotFounException;

    Reserva createReserva(Date fecha, List<ReservaHabitacionDTO> reservaHabitaciones, UsuarioDTO usuario) 
            throws ReservaNotFounException, HabitacionNotFoundException, UsuarioNotFoundException;

    Reserva updateReserva(Long reservaId, Date fecha, List<ReservaHabitacionDTO> reservaHabitaciones) 
            throws ReservaNotFounException, HabitacionNotFoundException, UsuarioNotFoundException;

    void deleteReserva(Long reservaId) throws ReservaNotFounException;

    ReservaDTO reservaToReservaDTO(Reserva reserva);
    
    // Nuevo método para crear reserva desde el carrito
    ReservaDTO createReservaFromCarrito(String username) 
            throws UsuarioNotFoundException, CarritoNotFoundException, CarritoEmptyException, 
                   HabitacionNotFoundException, ReservaNotFounException;
}
