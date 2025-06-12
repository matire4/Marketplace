package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.marketplace.entities.Carrito;
import com.uade.tpo.marketplace.entities.dto.CarritoDTO;
import com.uade.tpo.marketplace.exceptions.CarritoNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;
import com.uade.tpo.marketplace.service.CarritoService;

@RestController
@RequestMapping("/api/v1/carrito")
public class CarritoController {
    
    @Autowired
    private CarritoService carritoService;
    
    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<CarritoDTO> getCarritoByUsuario(@PathVariable String usuario) 
            throws CarritoNotFoundException, UsuarioNotFoundException {
        return ResponseEntity.ok(carritoService.getCarritoByUsuario(usuario));
    }
    
    @PostMapping("/usuario/{usuario}/habitacion/{habitacionId}")
    public ResponseEntity<CarritoDTO> addHabitacionToCarrito(
            @PathVariable String usuario,
            @PathVariable Long habitacionId,
            @RequestParam String nombreReserva) 
            throws CarritoNotFoundException, HabitacionNotFoundException, UsuarioNotFoundException {
        Carrito carrito = carritoService.addHabitacionToCarrito(usuario, habitacionId, nombreReserva);
        return ResponseEntity.ok(carritoService.carritoToCarritoDTO(carrito));
    }
    
    @DeleteMapping("/usuario/{usuario}/habitacion/{habitacionId}")
    public ResponseEntity<Void> removeHabitacionFromCarrito(
            @PathVariable String usuario,
            @PathVariable Long habitacionId) 
            throws CarritoNotFoundException, HabitacionNotFoundException, UsuarioNotFoundException {
        carritoService.removeHabitacionFromCarrito(usuario, habitacionId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/usuario/{usuario}")
    public ResponseEntity<Void> clearCarrito(@PathVariable String usuario) 
            throws CarritoNotFoundException, UsuarioNotFoundException {
        carritoService.clearCarrito(usuario);
        return ResponseEntity.noContent().build();
    }
}