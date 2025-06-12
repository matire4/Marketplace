package com.uade.tpo.marketplace.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import com.uade.tpo.marketplace.entities.dto.ReservaDTO;
import com.uade.tpo.marketplace.entities.dto.UsuarioDTO;
import com.uade.tpo.marketplace.entities.dto.ReservaHabitacionDTO;
import com.uade.tpo.marketplace.entities.Reserva;
import com.uade.tpo.marketplace.entities.ReservaHabitacion;
import com.uade.tpo.marketplace.entities.Usuario;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.exceptions.ReservaNotFounException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;
import com.uade.tpo.marketplace.repository.ReservaRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaServiceImpl implements ReservaService {
    @Autowired
    private ReservaRepository reservaRepository; 
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private HabitacionService habitacionService;
    @Autowired
    private ReservaHabitacionService reservaHabitacionService;

    @Override
    public List<ReservaDTO> getReservas() {
        List<Reserva> reservas = reservaRepository.findAll();
        return reservas.stream().map(reserva -> this.reservaToReservaDTO(reserva)).toList();
    }

    @Override
    public List<ReservaDTO> getReservasByUsuario(Long usuarioId) throws ReservaNotFounException, UsuarioNotFoundException {
        Optional<Usuario> optionalUsuario = usuarioService.getUsuarioById(usuarioId);
        if (optionalUsuario.isEmpty()) {
            throw new UsuarioNotFoundException();
        }
        List<Reserva> reservas = reservaRepository.findByUsuarioId(optionalUsuario.get().getId());
        return reservas.stream().map(reserva -> this.reservaToReservaDTO(reserva)).toList();
    }

    @Override
    public Optional<Reserva> getReservaById(Long reservaId) throws ReservaNotFounException {
        return reservaRepository.findById(reservaId);
    }

    @Override
    @Transactional
    public Reserva createReserva(Date fecha, List<ReservaHabitacionDTO> ReservaHabitaciones, UsuarioDTO usuario)
     throws ReservaNotFounException, HabitacionNotFoundException, UsuarioNotFoundException {
        List<ReservaHabitacion> reservaHabitacionesEntities = ReservaHabitaciones.stream()
            .map((ReservaHabitacionDTO reservaHabitacionDTO) -> {
                try {
                    return ReservaHabitacion.builder()
                        .nombreReserva(reservaHabitacionDTO.getNombreReserva())
                        .fechaDesde(reservaHabitacionDTO.getFechaDesde())
                        .fechaHasta(reservaHabitacionDTO.getFechaHasta())
                        .estado(reservaHabitacionDTO.getEstado())
                        .habitacion(habitacionService.getHabitacionByNombreHotelAndNumeroHabitacion(reservaHabitacionDTO.getHabitacionReserva().getHotelNombre(), reservaHabitacionDTO.getHabitacionReserva().getNumeroHabitacion()).get())
                        .cantidadPersonas(reservaHabitacionDTO.getCantidadPersonas())
                        .precio(reservaHabitacionDTO.getPrecio())
                        .build();
                } catch (HabitacionNotFoundException e) {
                    throw new RuntimeException(e);
                }
            })
            .toList();
        Usuario usuarioEntity = usuarioService.getUsuarioById(usuario.getId()).orElseThrow();
        Reserva reserva = new Reserva(fecha, reservaHabitacionesEntities, usuarioEntity); 
        return reservaRepository.save(reserva);
    }

    @Override
    public Reserva updateReserva(Long reservaId, Date fecha, List<ReservaHabitacionDTO> reservaHabitaciones, UsuarioDTO usuario) throws ReservaNotFounException {
        Optional<Reserva> optionalReserva = reservaRepository.findById(reservaId);
            Reserva reserva = optionalReserva.get();
            return reservaRepository.save(reserva);
    }

    @Override
    public void deleteReserva(Long reservaId) throws ReservaNotFounException {
        Optional<Reserva> optionalReserva = reservaRepository.findById(reservaId);
        reservaRepository.delete(optionalReserva.get());
    }

    @Override
    public ReservaDTO reservaToReservaDTO(Reserva reserva) {
        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setId(reserva.getId());
        reservaDTO.setFecha(reserva.getFecha());
        reservaDTO.setUsuarioDTO(usuarioService.usuarioToUsuarioDTO(reserva.getUsuario()));
        reservaDTO.setHabitaciones(
            reserva.getReservasHabitacion().stream()
            .map(reservaHabitacion -> reservaHabitacionService.reservaHabitacionToReservaHabitacionDTO(reservaHabitacion))
            .toList()
        );
        return reservaDTO;
    }
}
