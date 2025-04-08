package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entities.Hotel;
import com.uade.tpo.marketplace.exceptions.HotelDuplicateException;
import com.uade.tpo.marketplace.repository.HotelRepository;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    private HotelRepository hotelRepository;

    public List<Hotel> getHotels() {
        return hotelRepository.findAll();
    }

    public Optional<Hotel> getHotelById(Long hotelId) {
        return hotelRepository.findById(hotelId);
    }

    public Hotel createHotel(String nombre,
            String telefono,
            String email,
            String description,
            String direccion,
            String ciudad,
            String pais) throws HotelDuplicateException {
        List<Hotel> hoteles = hotelRepository.findByEmail(email);
        if (hoteles.isEmpty())
            return hotelRepository.save(new Hotel(
                    nombre,
                    telefono,
                    email,
                    description,
                    direccion,
                    ciudad,
                    pais));
        throw new HotelDuplicateException();
    }
}
