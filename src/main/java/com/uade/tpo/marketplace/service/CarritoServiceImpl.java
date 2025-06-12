package com.uade.tpo.marketplace.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entities.Carrito;
import com.uade.tpo.marketplace.entities.CarritoHabitacion;
import com.uade.tpo.marketplace.entities.Habitacion;
import com.uade.tpo.marketplace.entities.Usuario;
import com.uade.tpo.marketplace.entities.dto.CarritoDTO;
import com.uade.tpo.marketplace.exceptions.CarritoNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;
import com.uade.tpo.marketplace.repository.CarritoRepository;
import com.uade.tpo.marketplace.repository.HabitacionRepository;

@Service
public class CarritoServiceImpl implements CarritoService {
    
    @Autowired
    private CarritoRepository carritoRepository;
    
    @Autowired
    private HabitacionRepository habitacionRepository;
    
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private HabitacionService habitacionService;

    @Autowired
    private DepartamentoService departamentoService;

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
    public Carrito addHabitacionToCarrito(String usuario, Long habitacionId, String nombreReserva) 
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
        boolean habitacionExists = carrito.getCarritoHabitacions().stream()
                .anyMatch(ch -> ch.getHabitacion().getId().equals(habitacionId));
                
        if (!habitacionExists) {
            CarritoHabitacion carritoHabitacion = new CarritoHabitacion(nombreReserva, carrito, habitacion);
            carrito.getCarritoHabitacions().add(carritoHabitacion);
            return carritoRepository.save(carrito);
        }
        
        return carrito;
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
        CarritoDTO carritoDTO = new CarritoDTO();
        carritoDTO.setId(carrito.getId());
        carritoDTO.setUsuarioId(carrito.getUsuario().getId());

        if (carrito.getCarritoHabitacions() != null)
            carritoDTO.setHabitaciones(carrito.getCarritoHabitacions()
                            .stream()
                            .map(habitacion -> habitacionService.habitacionToHabitacionDTO(habitacion.getHabitacion()))
                            .toList());

        if (carrito.getCarritoDepartamentos() != null)
            carritoDTO.setDepartamentos(carrito.getCarritoDepartamentos()
                            .stream()
                            .map(departamento -> departamentoService.departamentoToDepartamentoDTO(departamento.getDepartamento()))
                            .toList());

        return carritoDTO;
    }
}