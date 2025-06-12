package com.uade.tpo.marketplace.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

import jakarta.transaction.Transactional;

@Service
public class CarritoServiceImpl implements CarritoService {
    
    @Autowired
    private CarritoRepository carritoRepository;
    
    @Autowired
    private HabitacionRepository habitacionRepository;
    
    @Autowired
    private UsuarioService usuarioService;

    @Override
    public CarritoDTO getCarritoByUsuario(Long usuarioId) throws CarritoNotFoundException, UsuarioNotFoundException {
        Usuario usuario = usuarioService.getUsuarioById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException());
                
        Carrito carrito = carritoRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Carrito newCarrito = new Carrito();
                    newCarrito.setUsuario(usuario);
                    newCarrito.setCarritoHabitacions(new ArrayList<>());
                    return carritoRepository.save(newCarrito);
                });
                
        return carritoToCarritoDTO(carrito);
    }

    @Override
    @Transactional
    public Carrito addHabitacionToCarrito(Long usuarioId, Long habitacionId, String nombreReserva) 
            throws CarritoNotFoundException, HabitacionNotFoundException, UsuarioNotFoundException {
        Usuario usuario = usuarioService.getUsuarioById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException());
                
        Habitacion habitacion = habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new HabitacionNotFoundException());
                
        Carrito carrito = carritoRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Carrito newCarrito = new Carrito();
                    newCarrito.setUsuario(usuario);
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
    public void removeHabitacionFromCarrito(Long usuarioId, Long habitacionId) 
            throws CarritoNotFoundException, HabitacionNotFoundException, UsuarioNotFoundException {
        Usuario usuario = usuarioService.getUsuarioById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException());
                
        Carrito carrito = carritoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new CarritoNotFoundException());
                
        carrito.getCarritoHabitacions().removeIf(ch -> ch.getHabitacion().getId().equals(habitacionId));
        carritoRepository.save(carrito);
    }

    @Override
    @Transactional
    public void clearCarrito(Long usuarioId) throws CarritoNotFoundException, UsuarioNotFoundException {
        Usuario usuario = usuarioService.getUsuarioById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException());
                
        Carrito carrito = carritoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new CarritoNotFoundException());
                
        carrito.getCarritoHabitacions().clear();
        carritoRepository.save(carrito);
    }

    @Override
    public CarritoDTO carritoToCarritoDTO(Carrito carrito) {
        CarritoDTO carritoDTO = new CarritoDTO();
        carritoDTO.setId(carrito.getId());
        carritoDTO.setUsuarioId(carrito.getUsuario().getId());
        
        if (carrito.getCarritoHabitacions() != null) {
            carritoDTO.setHabitacionesIds(
                carrito.getCarritoHabitacions().stream()
                    .map(ch -> ch.getHabitacion().getId())
                    .toList()
            );
        }
        
        return carritoDTO;
    }
}