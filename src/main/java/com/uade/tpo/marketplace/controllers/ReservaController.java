package com.uade.tpo.marketplace.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entities.Reserva;
import com.uade.tpo.marketplace.entities.dto.ReservaDTO;
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
    
    @GetMapping("/user/{usuario}")
    public ResponseEntity<List<ReservaDTO>> getReservasByUsuario(@PathVariable String usuario)
            throws ReservaNotFounException, UsuarioNotFoundException {
        List<ReservaDTO> result = reservaService.getReservasByUsuario(usuario);
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
                reservaDTO.getHabitaciones(), reservaDTO.getUsuarioDTO());
        return ResponseEntity.ok(reservaService.reservaToReservaDTO(result));
    }

    @PostMapping
    public ResponseEntity<ReservaDTO> createHabitacion(@RequestBody ReservaDTO reservaRequest) throws ReservaNotFounException,
            HabitacionNotFoundException, UsuarioNotFoundException {
        Reserva result = reservaService.createReserva(reservaRequest.getFecha(),
                reservaRequest.getHabitaciones(), reservaRequest.getUsuarioDTO());

        return ResponseEntity.created(URI.create("/reserva" + result.getId()))
                .body(reservaService.reservaToReservaDTO(result));
    }

}
