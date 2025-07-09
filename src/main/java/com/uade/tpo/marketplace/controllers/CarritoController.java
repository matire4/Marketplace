package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.uade.tpo.marketplace.entities.dto.CarritoDTO;
import com.uade.tpo.marketplace.entities.dto.CarritoDepartamentoDTO;
import com.uade.tpo.marketplace.entities.dto.CarritoHabitacionDTO;
import com.uade.tpo.marketplace.entities.dto.CarritoRequestDTO;
import com.uade.tpo.marketplace.exceptions.CarritoNotFoundException;
import com.uade.tpo.marketplace.exceptions.DepartamentoNotFoundException;
import com.uade.tpo.marketplace.exceptions.FechaYaReservadaException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;
import com.uade.tpo.marketplace.service.CarritoService;

@RestController
@RequestMapping("/api/v1/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping("/{usuario}")
    public ResponseEntity<CarritoDTO> getCarritoByUsuario(@PathVariable String usuario)
            throws CarritoNotFoundException, UsuarioNotFoundException {
        return ResponseEntity.ok(carritoService.getCarritoByUsuario(usuario));
    }

    @DeleteMapping("/{usuario}/{tipo}/{id}")
    public ResponseEntity<Void> removeFromCarrito(
            @PathVariable String usuario,
            @PathVariable Long id,
            @PathVariable String tipo)
            throws CarritoNotFoundException, HabitacionNotFoundException, UsuarioNotFoundException, DepartamentoNotFoundException {
        if ("HABITACION".equalsIgnoreCase(tipo)) {
            carritoService.removeHabitacionFromCarrito(usuario, id);
        } else if ("DEPARTAMENTO".equalsIgnoreCase(tipo)) {
            carritoService.removeDepartamentoFromCarrito(usuario, id) ;
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{usuario}")
    public ResponseEntity<Void> clearCarrito(@PathVariable String usuario)
            throws CarritoNotFoundException, UsuarioNotFoundException, FechaYaReservadaException {
        carritoService.clearCarrito(usuario);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{usuario}/{tipo}/{id}")
    public ResponseEntity<?> addToCarrito(
            @PathVariable String usuario,
            @PathVariable Long id,
            @PathVariable String tipo,
            @RequestBody CarritoRequestDTO request)
            throws CarritoNotFoundException, UsuarioNotFoundException,
            HabitacionNotFoundException, DepartamentoNotFoundException, FechaYaReservadaException {

        if ("HABITACION".equalsIgnoreCase(tipo)) {
            try {
                CarritoHabitacionDTO result = carritoService.addHabitacionToCarrito(
                        usuario, id, request.getNombreReserva(),
                        request.getCheckIn(), request.getCheckOut(),
                        request.getCantidad(), request.getPrecio());
                return ResponseEntity.ok(result);
            } catch (FechaYaReservadaException e) {
                ResponseStatus responseStatus = e.getClass().getAnnotation(ResponseStatus.class);
                return ResponseEntity.status(responseStatus.code()).body(responseStatus.reason());
            }
        } else if ("DEPARTAMENTO".equalsIgnoreCase(tipo)) {
            try {
                CarritoDepartamentoDTO result = carritoService.addDepartamentoToCarrito(
                    usuario, id, request.getNombreReserva(),
                    request.getCheckIn(), request.getCheckOut(), 
                    request.getCantidad(), request.getPrecio());
                return ResponseEntity.ok(result);
            } catch (FechaYaReservadaException e) {
                ResponseStatus responseStatus = e.getClass().getAnnotation(ResponseStatus.class);
                return ResponseEntity.status(responseStatus.code()).body(responseStatus.reason());
            }
        } else {
            return ResponseEntity.badRequest()
                    .body("Tipo de alojamiento no válido. Debe ser HABITACION o DEPARTAMENTO");
        }
    }
}