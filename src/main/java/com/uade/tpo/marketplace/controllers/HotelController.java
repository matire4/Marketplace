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
import com.uade.tpo.marketplace.entities.dto.DepartamentoDTO;
import com.uade.tpo.marketplace.entities.dto.HotelDTO;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.HotelDuplicateException;
import com.uade.tpo.marketplace.exceptions.HotelNotFoundException;
import com.uade.tpo.marketplace.service.DepartamentoService;
import com.uade.tpo.marketplace.service.HotelService;

@RestController
@RequestMapping("/api/v1/hoteles")
public class HotelController {
    @Autowired
    private HotelService hotelService;
    @Autowired
    private DepartamentoService departamentoService; // lo meto aca para manolo

    @GetMapping
    public ResponseEntity<List<HotelDTO>> getHotels() {
        return ResponseEntity.ok(hotelService.getHotels());
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDTO> getHotelById(@PathVariable Long hotelId)
            throws HotelNotFoundException {
        Optional<Hotel> result = hotelService.getHotelById(hotelId);
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
                hotelRequest.getGestorId(),
                hotelRequest.getCategoriaId(),
                hotelRequest.getHabitacionesParaCrear());

        return ResponseEntity.created(URI.create("/hoteles" + result.getId()))
                .body(hotelService.hotelToHotelDTO(result));
    }
}
