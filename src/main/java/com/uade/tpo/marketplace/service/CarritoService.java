package com.uade.tpo.marketplace.service;

import java.util.Optional;

import com.uade.tpo.marketplace.entities.Carrito;
import com.uade.tpo.marketplace.entities.dto.CarritoDTO;
import com.uade.tpo.marketplace.exceptions.CarritoNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;
import com.uade.tpo.marketplace.entities.dto.CarritoDepartamentoDTO;
import com.uade.tpo.marketplace.entities.dto.CarritoHabitacionDTO;
import com.uade.tpo.marketplace.exceptions.DepartamentoNotFoundException;
import com.uade.tpo.marketplace.exceptions.FechaYaReservadaException;

public interface CarritoService {
    CarritoDTO getCarritoByUsuario(String usuario) throws CarritoNotFoundException, UsuarioNotFoundException;

    // Nuevo método: obtiene la entidad Carrito en vez del DTO
    Carrito getCarritoEntityByUsuario(String usuario) throws CarritoNotFoundException, UsuarioNotFoundException;

    CarritoHabitacionDTO addHabitacionToCarrito(String usuario, Long habitacionId, String nombreReserva, String checkIn, String checkOut, int cantidad, double precio, String titularReserva)
            throws CarritoNotFoundException, HabitacionNotFoundException, UsuarioNotFoundException, FechaYaReservadaException;

    CarritoDepartamentoDTO addDepartamentoToCarrito(String usuario, Long departamentoId, String nombreReserva, String checkIn, String checkOut, int cantidad, double precio, String titularReserva)
            throws CarritoNotFoundException, DepartamentoNotFoundException, UsuarioNotFoundException, FechaYaReservadaException;

    void removeHabitacionFromCarrito(String usuario, Long habitacionId)
            throws CarritoNotFoundException, HabitacionNotFoundException, UsuarioNotFoundException;

    void removeDepartamentoFromCarrito(String usuario, Long id) 
            throws CarritoNotFoundException, DepartamentoNotFoundException, UsuarioNotFoundException;

    void clearCarrito(String usuario) throws CarritoNotFoundException, UsuarioNotFoundException;

    CarritoDTO carritoToCarritoDTO(Carrito carrito);

    Optional<Carrito> findById(Long carritoId);

    Carrito getCarritoById(Long carritoId) throws CarritoNotFoundException;
}