package com.uade.tpo.marketplace.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entities.Carrito;
import com.uade.tpo.marketplace.entities.CarritoDepartamento;
import com.uade.tpo.marketplace.entities.CarritoHabitacion;
import com.uade.tpo.marketplace.entities.Gestor;
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
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.exceptions.ReservaNotFounException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;
import com.uade.tpo.marketplace.repository.CarritoDepartamentoRepository;
import com.uade.tpo.marketplace.repository.CarritoHabitacionRepository;
import com.uade.tpo.marketplace.repository.ReservaDepartamentoRepository;
import com.uade.tpo.marketplace.repository.ReservaHabitacionRepository;
import com.uade.tpo.marketplace.repository.ReservaRepository;

@Service
public class ReservaServiceImpl implements ReservaService {
    @Autowired
    private ReservaRepository reservaRepository; 
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private GestorService gestorService;
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
    
    @Autowired
    private ReservaHabitacionRepository reservaHabitacionRepository;
    
    @Autowired
    private ReservaDepartamentoRepository reservaDepartamentoRepository;    

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
        reservaDTO.setUsuario(reserva.getUsuario().getUsername());
        System.out.println("ReservaDTO: " + reservaDTO);

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
                rh.setTitularReserva(ch.getTitularReserva());
                
                reserva.getReservasHabitacion().add(rh);
                precioTotal += ch.getPrecio();
                
                carritoHabitacionRepository.deleteById(ch.getId());
                carritoHabitacionRepository.flush();
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
                rd.setTitularReserva(cd.getTitularReserva());
                
                reserva.getReservasDepartamento().add(rd);
                precioTotal += cd.getPrecio();
        
                cd.getDepartamento().getCarritoDepartamento().remove(cd);
                carrito.getCarritoDepartamentos().remove(cd);

                carritoDepartamentoRepository.delete(cd);
                carritoDepartamentoRepository.flush();
            }
        }
        
        reserva.setPrecio(precioTotal);
        
        reserva = reservaRepository.save(reserva);
        
        return reservaToReservaDTO(reserva);
    }

    @Override
    public List<ReservaDTO> getReservasByGestor(String gestor)
            throws ReservaNotFounException, HabitacionNotFoundException, GestorNotFoundException {
        Optional<Gestor> optionalGestor = gestorService.getGestorByUsername(gestor);
        if (optionalGestor.isEmpty()) {
            throw new GestorNotFoundException();
        }
        
        List<ReservaDepartamento> reservasDepartamento = reservaDepartamentoService.findByGestorId(optionalGestor.get().getId());
        List<ReservaHabitacion> reservasHabitacion = reservaHabitacionService.findByGestorId(optionalGestor.get().getId());
        
        Map<Long, List<ReservaDepartamento>> departamentosPorReserva = reservasDepartamento.stream()
            .collect(Collectors.groupingBy(rd -> rd.getReserva().getId()));
        
        Map<Long, List<ReservaHabitacion>> habitacionesPorReserva = reservasHabitacion.stream()
            .collect(Collectors.groupingBy(rh -> rh.getReserva().getId()));
        
        List<Long> reservaIds = new ArrayList<>();
        reservaIds.addAll(departamentosPorReserva.keySet());
        reservaIds.addAll(habitacionesPorReserva.keySet());
        reservaIds = reservaIds.stream().distinct().collect(Collectors.toList());

        List<ReservaDTO> reservaDTOs = new ArrayList<>();
        for (Long reservaId : reservaIds) {
            Optional<Reserva> optionalReserva = reservaRepository.findById(reservaId);
            if (optionalReserva.isPresent()) {
                Reserva reserva = optionalReserva.get();
                ReservaDTO reservaDTO = new ReservaDTO();
                reservaDTO.setId(reserva.getId());
                reservaDTO.setFecha(reserva.getFecha());
                reservaDTO.setPrecio(reserva.getPrecio());
                reservaDTO.setUsuario(reserva.getUsuario().getUsername());
                
                if (departamentosPorReserva.containsKey(reservaId)) {
                    reservaDTO.setDepartamentos(
                        departamentosPorReserva.get(reservaId).stream()
                            .map(rd -> reservaDepartamentoService.reservaDepartamentoToReservaDepartamentoDTO(rd))
                            .collect(Collectors.toList())
                    );
                }
                
                if (habitacionesPorReserva.containsKey(reservaId)) {
                    reservaDTO.setHabitaciones(
                        habitacionesPorReserva.get(reservaId).stream()
                            .map(rh -> reservaHabitacionService.reservaHabitacionToReservaHabitacionDTO(rh))
                            .collect(Collectors.toList())
                    );
                }
                
                reservaDTOs.add(reservaDTO);
            }
        }
        
        return reservaDTOs;
    }
    
    @Override
    @Transactional
    public void finalizarReserva(String tipo, Long itemId) throws ReservaNotFounException {
        if ("habitacion".equalsIgnoreCase(tipo)) {
            // Finalizar ReservaHabitacion específica
            Optional<ReservaHabitacion> reservaHabitaciones = reservaHabitacionRepository.findById(itemId);
            if (reservaHabitaciones.isPresent()) {
                ReservaHabitacion rh = reservaHabitaciones.get();
                rh.setEstado(Estado.finalizado);
                reservaHabitacionRepository.save(rh);
            }
        } else if ("departamento".equalsIgnoreCase(tipo)) {
            // Finalizar ReservaDepartamento específica
            Optional<ReservaDepartamento> reservaDepartamento = reservaDepartamentoRepository.findById(itemId);
            if (reservaDepartamento.isPresent()) {
                ReservaDepartamento rd = reservaDepartamento.get();
                rd.setEstado(Estado.finalizado);
                reservaDepartamentoRepository.save(rd);
            }
        }
    }
}
