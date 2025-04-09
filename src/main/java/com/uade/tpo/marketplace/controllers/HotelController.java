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

import com.uade.tpo.marketplace.entities.Hotel;
import com.uade.tpo.marketplace.entities.dto.HotelRequest;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.HotelDuplicateException;
import com.uade.tpo.marketplace.service.HotelService;

@RestController
@RequestMapping("hoteles")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<Hotel>> getHotels() {
        return ResponseEntity.ok(hotelService.getHotels());
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long hotelId) {
        Optional<Hotel> result = hotelService.getHotelById(hotelId);
        if (result.isPresent())
            return ResponseEntity.ok(result.get());
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Hotel> createHotel(@RequestBody HotelRequest hotelRequest) throws HotelDuplicateException,
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
                hotelRequest.getGestorId(),
                hotelRequest.getCategoriaId());

        return ResponseEntity.created(URI.create("/hoteles" + result.getId())).body(result);
    }
}
