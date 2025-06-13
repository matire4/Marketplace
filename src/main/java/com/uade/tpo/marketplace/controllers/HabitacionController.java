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

import com.uade.tpo.marketplace.entities.Habitacion;
import com.uade.tpo.marketplace.entities.dto.HabitacionDTO;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionDuplicateException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.exceptions.HotelNotFoundException;
import com.uade.tpo.marketplace.exceptions.ImagenNotFoundException;
import com.uade.tpo.marketplace.service.HabitacionService;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/habitacion")
public class HabitacionController {
    @Autowired
    private HabitacionService habitacionService;

    @GetMapping
    public ResponseEntity<List<HabitacionDTO>> getHabitacion() {
        return ResponseEntity.ok(habitacionService.getHabitaciones());
    }

    @GetMapping("/{nombreHotel}")
    public ResponseEntity<List<HabitacionDTO>> getHabitacionByHotel(@PathVariable String nombreHotel)
            throws HabitacionNotFoundException {
        return ResponseEntity.ok(habitacionService.getHabitacionesByHotel(nombreHotel));
    }

    @GetMapping("/{nombreHotel}/{numeroHabitacion}")
    public ResponseEntity<HabitacionDTO> getHabitacionById(@PathVariable String nombreHotel,
            @PathVariable String numeroHabitacion)
            throws HabitacionNotFoundException {
        Optional<Habitacion> result = habitacionService.getHabitacionByNombreHotelAndNumeroHabitacion(nombreHotel,
                numeroHabitacion);
        if (result.isPresent())
            return ResponseEntity.ok(habitacionService.habitacionToHabitacionDTO(result.get()));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{nombreHotel}/{numeroHabitacion}")
    public ResponseEntity<Void> deleteHabitacion(@PathVariable String nombreHotel,
            @PathVariable String numeroHabitacion)
            throws HabitacionNotFoundException {
        Optional<Habitacion> result = habitacionService.getHabitacionByNombreHotelAndNumeroHabitacion(nombreHotel,
                numeroHabitacion);
        if (result.isPresent()) {
            habitacionService.deleteHabitacion(nombreHotel, numeroHabitacion);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{nombreHotel}/{numeroHabitacion}")
    public ResponseEntity<HabitacionDTO> updateHabitacion(@PathVariable String nombreHotel,
            @PathVariable String numeroHabitacion,
            @RequestBody HabitacionDTO habitacionRequest) throws HabitacionNotFoundException,
            GestorNotFoundException,
            CategoriaNotFoundException, ImagenNotFoundException {
        Habitacion result = habitacionService.updateHabitacion(nombreHotel, numeroHabitacion, habitacionRequest);
        return ResponseEntity.ok(habitacionService.habitacionToHabitacionDTO(result));
    }

    @PostMapping
    public ResponseEntity<HabitacionDTO> createHabitacion(@RequestBody HabitacionDTO habitacionRequest)
            throws HabitacionNotFoundException,
            GestorNotFoundException,
            CategoriaNotFoundException, HotelNotFoundException, HabitacionDuplicateException {
        Habitacion result = habitacionService.createHabitacion(
                habitacionRequest.getTipoHabitacion(),
                habitacionRequest.getCapacidad(),
                habitacionRequest.getPrecioPorNoche(),
                habitacionRequest.getNumeroHabitacion(),
                habitacionRequest.getImagenes(),
                habitacionRequest.getGestor(),
                habitacionRequest.getCategoria(),
                habitacionRequest.getAmbientes(),
                habitacionRequest.getBanos(),
                habitacionRequest.getDormitorios(),
                habitacionRequest.getCamas(),
                habitacionRequest.getHotel(),
                habitacionRequest.getReservas(),
                habitacionRequest.getCarritos());

        return ResponseEntity.created(URI.create("/habitaciones" + result.getId()))
                .body(habitacionService.habitacionToHabitacionDTO(result));
    }

}
