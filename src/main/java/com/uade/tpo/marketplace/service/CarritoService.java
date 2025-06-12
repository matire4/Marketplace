package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.entities.Carrito;
import com.uade.tpo.marketplace.entities.dto.CarritoDTO;
import com.uade.tpo.marketplace.exceptions.CarritoNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;

public interface CarritoService {
    CarritoDTO getCarritoByUsuario(Long usuarioId) throws CarritoNotFoundException, UsuarioNotFoundException;
    
    Carrito addHabitacionToCarrito(Long usuarioId, Long habitacionId, String nombreReserva) 
        throws CarritoNotFoundException, HabitacionNotFoundException, UsuarioNotFoundException;
    
    void removeHabitacionFromCarrito(Long usuarioId, Long habitacionId) 
        throws CarritoNotFoundException, HabitacionNotFoundException, UsuarioNotFoundException;
    
    void clearCarrito(Long usuarioId) throws CarritoNotFoundException, UsuarioNotFoundException;
    
    CarritoDTO carritoToCarritoDTO(Carrito carrito);
}