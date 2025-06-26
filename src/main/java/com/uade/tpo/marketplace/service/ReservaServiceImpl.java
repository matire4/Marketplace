package com.uade.tpo.marketplace.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entities.Carrito;
import com.uade.tpo.marketplace.entities.CarritoDepartamento;
import com.uade.tpo.marketplace.entities.CarritoHabitacion;
import com.uade.tpo.marketplace.entities.Reserva;
import com.uade.tpo.marketplace.entities.ReservaDepartamento;
import com.uade.tpo.marketplace.entities.ReservaHabitacion;
import com.uade.tpo.marketplace.entities.Usuario;
import com.uade.tpo.marketplace.entities.dto.ReservaDTO;
import com.uade.tpo.marketplace.entities.dto.ReservaHabitacionDTO;
import com.uade.tpo.marketplace.entities.dto.UsuarioDTO;
import com.uade.tpo.marketplace.enums.Estado;
import com.uade.tpo.marketplace.exceptions.CarritoEmptyException;
import com.uade.tpo.marketplace.exceptions.CarritoNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.exceptions.ReservaNotFounException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;
import com.uade.tpo.marketplace.repository.CarritoDepartamentoRepository;
import com.uade.tpo.marketplace.repository.CarritoHabitacionRepository;
import com.uade.tpo.marketplace.repository.ReservaRepository;

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
    @Autowired
    private ReservaDepartamentoService reservaDepartamentoService;
    @Autowired
    private CarritoService carritoService;
    @Autowired
    private CarritoHabitacionRepository carritoHabitacionRepository;

    @Autowired
    private CarritoDepartamentoRepository carritoDepartamentoRepository;    

    @Override
    public List<ReservaDTO> getReservas() {
        List<Reserva> reservas = reservaRepository.findAll();
        return reservas.stream().map(reserva -> this.reservaToReservaDTO(reserva)).toList();
    }

    @Override
    public List<ReservaDTO> getReservasByUsuario(String usuario) throws ReservaNotFounException, UsuarioNotFoundException {
        Optional<Usuario> optionalUsuario = usuarioService.getUsuarioByUsername(usuario);
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
        
        // Corrección de línea 71 - Usar findById en lugar de usar un objeto complejo
        List<ReservaHabitacion> reservaHabitacionesEntities = ReservaHabitaciones.stream()
            .map((ReservaHabitacionDTO reservaHabitacionDTO) -> {
                try {
                    return ReservaHabitacion.builder()
                        .nombreReserva(reservaHabitacionDTO.getNombreReserva())
                        .fechaDesde(reservaHabitacionDTO.getFechaDesde())
                        .fechaHasta(reservaHabitacionDTO.getFechaHasta())
                        .estado(reservaHabitacionDTO.getEstado())
                        .habitacion(habitacionService.getHabitacionById(reservaHabitacionDTO.getHabitacionId())
                                .orElseThrow(() -> new RuntimeException("Habitación no encontrada")))
                        .cantidadPersonas(reservaHabitacionDTO.getCantidadPersonas())
                        .precio(reservaHabitacionDTO.getPrecio())
                        .build();
                } catch (RuntimeException e) {
                    throw new RuntimeException(e);
                }
            })
            .collect(Collectors.toList());
        
        // Corrección de línea 78 - Manejo adecuado de Optional
        Usuario usuarioEntity = usuarioService.getUsuarioByUsername(usuario.getUsername())
                .orElseThrow(() -> new UsuarioNotFoundException());
        
        Reserva reserva = new Reserva();
        reserva.setFecha(fecha);
        reserva.setUsuario(usuarioEntity);
        reserva.setReservasHabitacion(reservaHabitacionesEntities);
        reserva.setPrecio(reservaHabitacionesEntities.stream().mapToDouble(ReservaHabitacion::getPrecio).sum());
        
        return reservaRepository.save(reserva);
    }

    @Override
    public Reserva updateReserva(Long reservaId, Date fecha, List<ReservaHabitacionDTO> reservaHabitaciones) throws ReservaNotFounException {
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
        reservaDTO.setPrecio(reserva.getPrecio());
        
        // Incluir habitaciones si existen
        if (reserva.getReservasHabitacion() != null) {
            reservaDTO.setHabitaciones(
                reserva.getReservasHabitacion().stream()
                .map(reservaHabitacion -> reservaHabitacionService.reservaHabitacionToReservaHabitacionDTO(reservaHabitacion))
                .toList()
            );
        }
        
        // Incluir departamentos si existen
        if (reserva.getReservasDepartamento() != null) {
            reservaDTO.setDepartamentos(
                reserva.getReservasDepartamento().stream()
                .map(reservaDepartamento -> reservaDepartamentoService.reservaDepartamentoToReservaDepartamentoDTO(reservaDepartamento))
                .toList()
            );
        }
        
        return reservaDTO;
    }

    @Override
    @Transactional
    public ReservaDTO createReservaFromCarrito(String username) 
            throws UsuarioNotFoundException, CarritoNotFoundException, CarritoEmptyException, 
                HabitacionNotFoundException, ReservaNotFounException {
        
        Usuario usuario = usuarioService.getUsuarioByUsername(username)
                .orElseThrow(() -> new UsuarioNotFoundException());
        
        Carrito carrito = carritoService.getCarritoEntityByUsuario(username);
        
        boolean carritoVacio = (carrito.getCarritoHabitacions() == null || carrito.getCarritoHabitacions().isEmpty()) 
                            && (carrito.getCarritoDepartamentos() == null || carrito.getCarritoDepartamentos().isEmpty());
                            
        if (carritoVacio) {
            throw new CarritoEmptyException();
        }
        
        Reserva reserva = new Reserva();
        reserva.setFecha(new Date(System.currentTimeMillis()));
        reserva.setUsuario(usuario);
        reserva.setReservasHabitacion(new ArrayList<>());
        reserva.setReservasDepartamento(new ArrayList<>());
        
        reserva = reservaRepository.save(reserva);
        
        double precioTotal = 0.0;
        
        if (carrito.getCarritoHabitacions() != null) {
            for (CarritoHabitacion ch : new ArrayList<>(carrito.getCarritoHabitacions())) {
                ReservaHabitacion rh = new ReservaHabitacion();
                rh.setNombreReserva(ch.getNombreReserva());
                rh.setReserva(reserva);
                rh.setHabitacion(ch.getHabitacion());
                rh.setFechaDesde(ch.getCheckIn());
                rh.setFechaHasta(ch.getCheckOut());
                rh.setCantidadPersonas(ch.getCantidad());
                rh.setPrecio(ch.getPrecio());
                rh.setEstado(Estado.aprobado);
                
                reserva.getReservasHabitacion().add(rh);
                precioTotal += ch.getPrecio();
                
                carritoHabitacionRepository.delete(ch);
            }
        }
        
        if (carrito.getCarritoDepartamentos() != null) {
            for (CarritoDepartamento cd : new ArrayList<>(carrito.getCarritoDepartamentos())) {
                ReservaDepartamento rd = new ReservaDepartamento();
                rd.setNombreReserva(cd.getNombreReserva());
                rd.setReserva(reserva);
                rd.setDepartamento(cd.getDepartamento());
                rd.setFechaDesde(cd.getCheckIn());
                rd.setFechaHasta(cd.getCheckOut());
                rd.setCantidadPersonas(cd.getCantidad());
                rd.setPrecio(cd.getPrecio());
                rd.setEstado(Estado.aprobado);
                
                reserva.getReservasDepartamento().add(rd);
                precioTotal += cd.getPrecio();
                
                carritoDepartamentoRepository.delete(cd);
            }
        }
        
        reserva.setPrecio(precioTotal);
        
        reserva = reservaRepository.save(reserva);
        
        return reservaToReservaDTO(reserva);
    }
}
