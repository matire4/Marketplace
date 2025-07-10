package com.uade.tpo.marketplace.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entities.Reserva;
import com.uade.tpo.marketplace.entities.dto.ReservaDTO;
import com.uade.tpo.marketplace.exceptions.CarritoEmptyException;
import com.uade.tpo.marketplace.exceptions.CarritoNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.exceptions.ReservaNotFounException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;
import com.uade.tpo.marketplace.service.ReservaService;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/reserva")
public class ReservaController {
    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public ResponseEntity<List<ReservaDTO>> getReservas() {
        return ResponseEntity.ok(reservaService.getReservas());
    }
    
    @GetMapping("/{usuario}/usuario")
    public ResponseEntity<List<ReservaDTO>> getReservasByUsuario(@PathVariable String usuario)
            throws ReservaNotFounException, UsuarioNotFoundException {
        List<ReservaDTO> result = reservaService.getReservasByUsuario(usuario);
        if (result != null && !result.isEmpty()) 
            return ResponseEntity.ok(result);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{gestor}/gestor")
    public ResponseEntity<List<ReservaDTO>> getReservasByGestor(@PathVariable String gestor)
            throws ReservaNotFounException, GestorNotFoundException, HabitacionNotFoundException {
        List<ReservaDTO> result = reservaService.getReservasByGestor(gestor);
        if (result != null && !result.isEmpty())
            return ResponseEntity.ok(result);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{reservaId}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Long reservaId)
            throws ReservaNotFounException {
        Optional<Reserva> result = reservaService.getReservaById(reservaId);
        if (result.isPresent()) {
            reservaService.deleteReserva(reservaId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{reservaId}")
    public ResponseEntity<ReservaDTO> updateHabitacion(@PathVariable Long reservaId,
            @RequestBody ReservaDTO reservaDTO) throws ReservaNotFounException,
            HabitacionNotFoundException, UsuarioNotFoundException {
            Reserva result = reservaService.updateReserva(reservaId, reservaDTO.getFecha(),
                reservaDTO.getHabitaciones());
        return ResponseEntity.ok(reservaService.reservaToReservaDTO(result));
    }

    @PostMapping("/{username}")
    public ResponseEntity<?> createReservaFromCarrito(@PathVariable String username) {
        try {
            ReservaDTO reservaDTO = reservaService.createReservaFromCarrito(username);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservaDTO);
        } catch (UsuarioNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado: " + username);
        } catch (CarritoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Carrito no encontrado para el usuario: " + username);
        } catch (CarritoEmptyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El carrito está vacío");
        } catch (HabitacionNotFoundException | ReservaNotFounException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la reserva: " + e.getMessage());
        }
    }

    @PutMapping("/{itemId}/finalizar")
    public ResponseEntity<String> finalizarReserva(@PathVariable Long itemId,
                                                  @RequestParam String tipo) {
        try {
            reservaService.finalizarReserva(tipo, itemId);
            return ResponseEntity.ok("Reserva finalizada exitosamente");
        } catch (ReservaNotFounException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva no encontrada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al finalizar la reserva: " + e.getMessage());
        }
    }
}
