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

import com.uade.tpo.marketplace.entities.Hotel;
import com.uade.tpo.marketplace.entities.dto.HotelDTO;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.HotelDuplicateException;
import com.uade.tpo.marketplace.exceptions.HotelNotFoundException;
import com.uade.tpo.marketplace.service.HotelService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/hoteles")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<HotelDTO>> getHotels() {
        return ResponseEntity.ok(hotelService.getHotels());
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<HotelDTO> getHotelByNombre(@PathVariable String nombre)
            throws HotelNotFoundException {
        Optional<Hotel> result = hotelService.getHotelByNombre(nombre);
        if (result.isPresent())
            return ResponseEntity.ok(hotelService.hotelToHotelDTO(result.get()));
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<HotelDTO> createHotel(@RequestBody HotelDTO hotelRequest) throws HotelDuplicateException,
            GestorNotFoundException,
            CategoriaNotFoundException {
        Hotel result = hotelService.createHotel(
                hotelRequest.getNombre(),
                hotelRequest.getTelefono(),
                hotelRequest.getEmail(),
                hotelRequest.getDescripcion(),
                hotelRequest.getDireccion(),
                hotelRequest.getCiudad(),
                hotelRequest.getPais(),
                hotelRequest.getUsername(),
                hotelRequest.getCategoria(),
                hotelRequest.getHabitaciones().stream().map(h -> h.getId()).toList(),
                hotelRequest.getHabitacionesParaCrear());

        return ResponseEntity.created(URI.create("/hoteles" + result.getId()))
                .body(hotelService.hotelToHotelDTO(result));
    }

    @DeleteMapping("/{nombre}")
    public ResponseEntity<Void> deleteHotel(@PathVariable String nombre) throws HotelNotFoundException {
        Optional<Hotel> result = hotelService.getHotelByNombre(nombre);
        if (result.isPresent()) {
            hotelService.deleteHotel(nombre);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{nombre}")
    public ResponseEntity<HotelDTO> updateHotel(@PathVariable String nombre, @RequestBody HotelDTO hotel)
            throws HotelNotFoundException, HotelDuplicateException {
        Optional<Hotel> result = hotelService.getHotelByNombre(nombre);
        if (result.isPresent()) {
            return ResponseEntity.ok(hotelService.updateHotel(nombre, hotel));
        }
        return ResponseEntity.notFound().build();
    }

}
