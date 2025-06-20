package com.uade.tpo.marketplace.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.marketplace.entities.CarritoHabitacion;
import com.uade.tpo.marketplace.entities.dto.CarritoDTO;
import com.uade.tpo.marketplace.entities.dto.CarritoDepartamentoDTO;
import com.uade.tpo.marketplace.entities.dto.CarritoHabitacionDTO;
import com.uade.tpo.marketplace.entities.dto.CarritoRequestDTO;
import com.uade.tpo.marketplace.exceptions.CarritoNotFoundException;
import com.uade.tpo.marketplace.exceptions.DepartamentoNotFoundException;
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
            throws CarritoNotFoundException, UsuarioNotFoundException {
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
            HabitacionNotFoundException, DepartamentoNotFoundException {

        if ("HABITACION".equalsIgnoreCase(tipo)) {
            CarritoHabitacionDTO result = carritoService.addHabitacionToCarrito(
                    usuario, id, request.getNombreReserva(),
                    request.getCheckIn(), request.getCheckOut(), 
                    request.getCantidad(), request.getPrecio());
            return ResponseEntity.ok(result);
        } else if ("DEPARTAMENTO".equalsIgnoreCase(tipo)) {
            CarritoDepartamentoDTO result = carritoService.addDepartamentoToCarrito(
                    usuario, id, request.getNombreReserva());
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest()
                    .body("Tipo de alojamiento no válido. Debe ser HABITACION o DEPARTAMENTO");
        }
    }
}