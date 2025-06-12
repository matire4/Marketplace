package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplace.entities.Carrito;
import com.uade.tpo.marketplace.entities.CarritoHabitacion;
import com.uade.tpo.marketplace.entities.Habitacion;
import com.uade.tpo.marketplace.entities.dto.CarritoHabitacionDTO;
import com.uade.tpo.marketplace.exceptions.CarritoNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.repository.CarritoHabitacionRepository;
import com.uade.tpo.marketplace.repository.CarritoRepository;
import com.uade.tpo.marketplace.repository.HabitacionRepository;

@Service
public class CarritoHabitacionServiceImpl implements CarritoHabitacionService {
    
    @Autowired
    private CarritoHabitacionRepository carritoHabitacionRepository;
    
    @Autowired
    private CarritoRepository carritoRepository;
    
    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private HabitacionService habitacionService;

    @Override
    @Transactional(readOnly = true)
    public List<CarritoHabitacionDTO> getCarritosHabitacion() {
        return carritoHabitacionRepository.findAll()
                .stream()
                .map(this::carritoHabitacionToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CarritoHabitacion> getCarritoHabitacionByUsuario(String username) throws CarritoNotFoundException {
        return carritoHabitacionRepository.findByCarritoUsuarioUsername(username);
    }

    @Override
    @Transactional
    public CarritoHabitacion createCarritoHabitacion(Long carritoId, Long habitacionId, String nombreReserva) 
            throws CarritoNotFoundException, HabitacionNotFoundException {
        
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new CarritoNotFoundException());
                
        Habitacion habitacion = habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new HabitacionNotFoundException());

        CarritoHabitacion carritoHabitacion = new CarritoHabitacion(nombreReserva, carrito, habitacion);
        return carritoHabitacionRepository.save(carritoHabitacion);
    }

    @Override
    @Transactional
    public void deleteCarritoHabitacion(Long id) throws CarritoNotFoundException {
        if (!carritoHabitacionRepository.existsById(id)) {
            throw new CarritoNotFoundException();
        }
        carritoHabitacionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarritoHabitacionDTO> getCarritosHabitacionByCarritoId(Long carritoId) {
        return carritoHabitacionRepository.findByCarritoId(carritoId)
                .stream()
                .map(this::carritoHabitacionToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarritoHabitacionDTO> getCarritosHabitacionByHabitacionId(Long habitacionId) {
        return carritoHabitacionRepository.findByHabitacionId(habitacionId)
                .stream()
                .map(this::carritoHabitacionToDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CarritoHabitacionDTO carritoHabitacionToDTO(CarritoHabitacion carritoHabitacion) {
        if (carritoHabitacion == null) {
            return null;
        }
        
        return new CarritoHabitacionDTO(carritoHabitacion.getId(),carritoHabitacion.getNombreReserva(),habitacionService.habitacionToHabitacionDTO(carritoHabitacion.getHabitacion()),carritoService.carritoToCarritoDTO(carritoHabitacion.getCarrito()));
    }
}