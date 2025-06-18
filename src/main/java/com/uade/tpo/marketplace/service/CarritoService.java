package com.uade.tpo.marketplace.service;

import com.uade.tpo.marketplace.entities.Carrito;
import com.uade.tpo.marketplace.entities.CarritoHabitacion;
import com.uade.tpo.marketplace.entities.dto.CarritoDTO;
import com.uade.tpo.marketplace.exceptions.CarritoNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;
import com.uade.tpo.marketplace.entities.dto.CarritoDepartamentoDTO;
import com.uade.tpo.marketplace.entities.dto.CarritoHabitacionDTO;
import com.uade.tpo.marketplace.exceptions.DepartamentoNotFoundException;

public interface CarritoService {
    CarritoDTO getCarritoByUsuario(String usuario) throws CarritoNotFoundException, UsuarioNotFoundException;

    CarritoHabitacionDTO addHabitacionToCarrito(String usuario, Long habitacionId, String nombreReserva)
            throws CarritoNotFoundException, HabitacionNotFoundException, UsuarioNotFoundException;

    void removeHabitacionFromCarrito(String usuario, Long habitacionId)
            throws CarritoNotFoundException, HabitacionNotFoundException, UsuarioNotFoundException;

    void clearCarrito(String usuario) throws CarritoNotFoundException, UsuarioNotFoundException;

    CarritoDTO carritoToCarritoDTO(Carrito carrito);

    CarritoDepartamentoDTO addDepartamentoToCarrito(String usuario, Long departamentoId, String nombreReserva)
            throws CarritoNotFoundException, DepartamentoNotFoundException, UsuarioNotFoundException;
}