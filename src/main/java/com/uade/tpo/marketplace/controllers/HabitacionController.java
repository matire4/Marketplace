package com.uade.tpo.marketplace.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entities.Habitacion;
import com.uade.tpo.marketplace.entities.dto.HabitacionDTO;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.service.HabitacionService;


@RestController
@RequestMapping("/api/v1/habitacion")
public class HabitacionController {
    @Autowired
    private HabitacionService habitacionService;

    @GetMapping
    public ResponseEntity<List<HabitacionDTO>> getHabitacion() {
        return ResponseEntity.ok(habitacionService.getHabitaciones());
    }
    
    @GetMapping("/{habitacionId}")
    public ResponseEntity<HabitacionDTO> getHabitacionById(@PathVariable Long habitacionId)
            throws HabitacionNotFoundException {
        Optional<Habitacion> result = habitacionService.getHabitacionById(habitacionId);
        if (result.isPresent())
            return ResponseEntity.ok(habitacionService.habitacionToHabitacionDTO(result.get()));
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<HabitacionDTO> createHabitacion(@RequestBody HabitacionDTO habitacionRequest) throws HabitacionNotFoundException,
            GestorNotFoundException,
            CategoriaNotFoundException {
        Habitacion result = habitacionService.createHabitacion(
                habitacionRequest.getTipoHabitacion(),
                habitacionRequest.getCapacidad(),
                habitacionRequest.getPrecioPorNoche(),
                habitacionRequest.getNumeroHabitacion(),
                habitacionRequest.getImagen(),
                habitacionRequest.getGestorId(),
                habitacionRequest.getCategoriaId());

        return ResponseEntity.created(URI.create("/habitaciones" + result.getId()))
                .body(habitacionService.habitacionToHabitacionDTO(result));
    }

}
