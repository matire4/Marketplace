package com.uade.tpo.marketplace.service;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entities.Carrito;
import com.uade.tpo.marketplace.entities.CarritoDepartamento;
import com.uade.tpo.marketplace.entities.CarritoHabitacion;
import com.uade.tpo.marketplace.entities.Departamento;
import com.uade.tpo.marketplace.entities.Habitacion;
import com.uade.tpo.marketplace.entities.Usuario;
import com.uade.tpo.marketplace.entities.dto.CarritoDTO;
import com.uade.tpo.marketplace.entities.dto.CarritoDepartamentoDTO;
import com.uade.tpo.marketplace.entities.dto.CarritoHabitacionDTO;
import com.uade.tpo.marketplace.entities.dto.DepartamentoDTO;
import com.uade.tpo.marketplace.entities.dto.HabitacionDTO;
import com.uade.tpo.marketplace.exceptions.CarritoNotFoundException;
import com.uade.tpo.marketplace.exceptions.DepartamentoNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;
import com.uade.tpo.marketplace.repository.CarritoDepartamentoRepository;
import com.uade.tpo.marketplace.repository.CarritoHabitacionRepository;
import com.uade.tpo.marketplace.repository.CarritoRepository;
import com.uade.tpo.marketplace.repository.DepartamentoRepository;
import com.uade.tpo.marketplace.repository.HabitacionRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class CarritoServiceImpl implements CarritoService {
    
    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoHabitacionRepository carritoHabitacionRepository;

    @Autowired
    CarritoHabitacionService carritoHabitacionService;
    
    @Autowired
    private HabitacionRepository habitacionRepository;
    
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private HabitacionService habitacionService;

    @Autowired
    private DepartamentoService departamentoService;
    
    @Autowired
    private DepartamentoRepository departamentoRepository;
    
    @Autowired
    private CarritoDepartamentoRepository carritoDepartamentoRepository;

    @Override
    @Transactional(readOnly = true)
    public CarritoDTO getCarritoByUsuario(String usuario) throws CarritoNotFoundException, UsuarioNotFoundException {
        Usuario u = usuarioService.getUsuarioByUsername(usuario)
                .orElseThrow(() -> new UsuarioNotFoundException());

        Carrito carrito = carritoRepository.findByUsuario(u)
                .orElseGet(() -> {
                    Carrito newCarrito = new Carrito();
                    newCarrito.setUsuario(u);
                    newCarrito.setCarritoHabitacions(new ArrayList<>());
                    return carritoRepository.save(newCarrito);
                });
                
        return carritoToCarritoDTO(carrito);
    }

    @Override
        @Transactional
        public CarritoHabitacionDTO addHabitacionToCarrito(String usuario, Long habitacionId, String nombreReserva, 
                String checkIn, String checkOut, int cantidad, double precio) 
                throws CarritoNotFoundException, HabitacionNotFoundException, UsuarioNotFoundException {
            Usuario u = usuarioService.getUsuarioByUsername(usuario)
                    .orElseThrow(() -> new UsuarioNotFoundException());
                    
            Habitacion habitacion = habitacionRepository.findById(habitacionId)
                    .orElseThrow(() -> new HabitacionNotFoundException());
                    
            Carrito carrito = carritoRepository.findByUsuario(u)
                    .orElseGet(() -> {
                        Carrito newCarrito = new Carrito();
                        newCarrito.setUsuario(u);
                        newCarrito.setCarritoHabitacions(new ArrayList<>());
                        return carritoRepository.save(newCarrito);
                    });

            // Verificar si la habitación ya está en el carrito
            Optional<CarritoHabitacion> existingItem = carrito.getCarritoHabitacions().stream()
                    .filter(ch -> ch.getHabitacion().getId().equals(habitacionId))
                    .findFirst();
                    
            if (existingItem.isPresent()) {
                // Si ya existe, devolver el DTO correspondiente
                return carritoHabitacionService.carritoHabitacionToDTO(existingItem.get());
            } else {
                // Convertir String a Date
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date checkInDate = null;
                Date checkOutDate = null;
                try {
                    checkInDate = new Date(format.parse(checkIn).getTime());
                    checkOutDate = new Date(format.parse(checkOut).getTime());
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Formato de fecha inválido. Use yyyy-MM-dd");
                }
                
                // Si no existe, crear nuevo
                CarritoHabitacion carritoHabitacion = new CarritoHabitacion();
                carritoHabitacion.setNombreReserva(nombreReserva);
                carritoHabitacion.setCarrito(carrito);
                carritoHabitacion.setHabitacion(habitacion);
                carritoHabitacion.setCantidad(cantidad);
                carritoHabitacion.setCheckIn(checkInDate);
                carritoHabitacion.setCheckOut(checkOutDate);
                carritoHabitacion.setPrecio(precio);
                
                carrito.getCarritoHabitacions().add(carritoHabitacion);
                CarritoHabitacion saved = carritoHabitacionRepository.save(carritoHabitacion);
                return carritoHabitacionService.carritoHabitacionToDTO(saved);
            }
        }

    @Override
    @Transactional
    public void removeHabitacionFromCarrito(String usuario, Long habitacionId) 
            throws CarritoNotFoundException, HabitacionNotFoundException, UsuarioNotFoundException {
        Usuario u = usuarioService.getUsuarioByUsername(usuario)
                .orElseThrow(() -> new UsuarioNotFoundException());

        Carrito carrito = carritoRepository.findByUsuario(u)
                .orElseThrow(() -> new CarritoNotFoundException());
                
        carrito.getCarritoHabitacions().removeIf(ch -> ch.getHabitacion().getId().equals(habitacionId));
        carritoRepository.save(carrito);
    }

    @Override
    @Transactional
    public void clearCarrito(String usuario) throws CarritoNotFoundException, UsuarioNotFoundException {
        Usuario u = usuarioService.getUsuarioByUsername(usuario)
                .orElseThrow(() -> new UsuarioNotFoundException());

        Carrito carrito = carritoRepository.findByUsuario(u)
                .orElseThrow(() -> new CarritoNotFoundException());
                
        carrito.getCarritoHabitacions().clear();
        carritoRepository.save(carrito);
    }

    @Override
    @Transactional(readOnly = true)
    public CarritoDTO carritoToCarritoDTO(Carrito carrito) {
        if (carrito == null) {
            return null;
        }

        CarritoDTO carritoDTO = new CarritoDTO();
        carritoDTO.setId(carrito.getId());
        
        if (carrito.getUsuario() != null) {
            carritoDTO.setUsuarioId(carrito.getUsuario().getId());
        }

        if (carrito.getCarritoHabitacions() != null) {
            List<HabitacionDTO> habitacionesDTO = carrito.getCarritoHabitacions().stream()
                    .map(ch -> ch.getHabitacion())
                    .filter(Objects::nonNull)
                    .map(habitacionService::habitacionToHabitacionDTO)
                    .filter(Objects::nonNull)
                    .toList();
            carritoDTO.setHabitaciones(habitacionesDTO);
        }
        
        if (carrito.getCarritoDepartamentos() != null) {
            List<DepartamentoDTO> departamentosDTO = carrito.getCarritoDepartamentos().stream()
                    .map(cd -> cd.getDepartamento())
                    .filter(Objects::nonNull)
                    .map(departamentoService::departamentoToDepartamentoDTO)
                    .filter(Objects::nonNull)
                    .toList();
            carritoDTO.setDepartamentos(departamentosDTO);
        }

        return carritoDTO;
    }

    @Override
    @Transactional
    public CarritoDepartamentoDTO addDepartamentoToCarrito(String usuario, Long departamentoId, String nombreReserva) 
            throws CarritoNotFoundException, DepartamentoNotFoundException, UsuarioNotFoundException {
        
        Usuario u = usuarioService.getUsuarioByUsername(usuario)
                .orElseThrow(() -> new UsuarioNotFoundException());
        
        Carrito carrito = carritoRepository.findByUsuario(u)
                .orElseThrow(() -> new CarritoNotFoundException());
        
        Departamento departamento = departamentoRepository.findById(departamentoId)
                .orElseThrow(() -> new DepartamentoNotFoundException());
        
        CarritoDepartamento carritoDepartamento = new CarritoDepartamento();
        carritoDepartamento.setNombreReserva(nombreReserva);
        carritoDepartamento.setCarrito(carrito);
        carritoDepartamento.setDepartamento(departamento);
        
        carritoDepartamentoRepository.save(carritoDepartamento);
        
        return CarritoDepartamentoDTO.builder()
                .id(carritoDepartamento.getId())
                .nombreReserva(carritoDepartamento.getNombreReserva())
                .carritoId(carrito.getId())
                .departamentoId(departamento.getId())
                .build();
    }

    @Override
    public void removeDepartamentoFromCarrito(String usuario, Long id) throws CarritoNotFoundException, UsuarioNotFoundException {
        Usuario u = usuarioService.getUsuarioByUsername(usuario)
                .orElseThrow(() -> new UsuarioNotFoundException());

        Carrito carrito = carritoRepository.findByUsuario(u)
                .orElseThrow(() -> new CarritoNotFoundException());

        carrito.getCarritoDepartamentos().removeIf(cd -> cd.getId().equals(id));
        carritoRepository.save(carrito);
    }
}