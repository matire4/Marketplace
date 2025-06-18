package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.marketplace.entities.Carrito;
import com.uade.tpo.marketplace.entities.CarritoHabitacion;
import com.uade.tpo.marketplace.entities.dto.CarritoDTO;
import com.uade.tpo.marketplace.entities.dto.CarritoDepartamentoDTO;
import com.uade.tpo.marketplace.entities.dto.CarritoHabitacionDTO;
import com.uade.tpo.marketplace.exceptions.CarritoNotFoundException;
import com.uade.tpo.marketplace.exceptions.DepartamentoNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;
import com.uade.tpo.marketplace.service.CarritoHabitacionService;
import com.uade.tpo.marketplace.service.CarritoService;

@RestController
@RequestMapping("/api/v1/carrito")
public class CarritoController {
    
    @Autowired
    private CarritoService carritoService;
    @Autowired
    private CarritoHabitacionService carritoHabitacionService;
    
    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<CarritoDTO> getCarritoByUsuario(@PathVariable String usuario) 
            throws CarritoNotFoundException, UsuarioNotFoundException {
        return ResponseEntity.ok(carritoService.getCarritoByUsuario(usuario));
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

    @PostMapping("/usuario/{usuario}/alojamiento")
    public ResponseEntity<?> addToCarrito(
            @PathVariable String usuario,
            @RequestParam Long alojamientoId,
            @RequestParam String tipo,
            @RequestParam String nombreReserva)
            throws CarritoNotFoundException, UsuarioNotFoundException, 
                   HabitacionNotFoundException, DepartamentoNotFoundException {
        
        if ("HABITACION".equalsIgnoreCase(tipo)) {
            CarritoHabitacionDTO result = carritoService.addHabitacionToCarrito(usuario, alojamientoId, nombreReserva);
            return ResponseEntity.ok(result);
        } else if ("DEPARTAMENTO".equalsIgnoreCase(tipo)) {
            CarritoDepartamentoDTO result = carritoService.addDepartamentoToCarrito(usuario, alojamientoId, nombreReserva);
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body("Tipo de alojamiento no válido. Debe ser HABITACION o DEPARTAMENTO");
        }
    }
}