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
    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CarritoDTO> getCarritoByUsuario(@PathVariable Long usuarioId) 
            throws CarritoNotFoundException, UsuarioNotFoundException {
        return ResponseEntity.ok(carritoService.getCarritoByUsuario(usuarioId));
    }
    
    @PostMapping("/usuario/{usuarioId}/habitacion/{habitacionId}")
    public ResponseEntity<CarritoDTO> addHabitacionToCarrito(
            @PathVariable Long usuarioId,
            @PathVariable Long habitacionId,
            @RequestParam String nombreReserva) 
            throws CarritoNotFoundException, HabitacionNotFoundException, UsuarioNotFoundException {
        Carrito carrito = carritoService.addHabitacionToCarrito(usuarioId, habitacionId, nombreReserva);
        return ResponseEntity.ok(carritoService.carritoToCarritoDTO(carrito));
    }
    
    @DeleteMapping("/usuario/{usuarioId}/habitacion/{habitacionId}")
    public ResponseEntity<Void> removeHabitacionFromCarrito(
            @PathVariable Long usuarioId,
            @PathVariable Long habitacionId) 
            throws CarritoNotFoundException, HabitacionNotFoundException, UsuarioNotFoundException {
        carritoService.removeHabitacionFromCarrito(usuarioId, habitacionId);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/usuario/{usuarioId}")
    public ResponseEntity<Void> clearCarrito(@PathVariable Long usuarioId) 
            throws CarritoNotFoundException, UsuarioNotFoundException {
        carritoService.clearCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }
}