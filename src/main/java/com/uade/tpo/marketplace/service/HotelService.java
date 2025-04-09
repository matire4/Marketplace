package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entities.Hotel;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.HotelDuplicateException;

public interface HotelService {
    public List<Hotel> getHotels();

    public Optional<Hotel> getHotelById(Long hotelId);

    public Hotel createHotel(String nombre,
            String telefono,
            String email,
            String description,
            String direccion,
            String ciudad,
            String pais,
            Long gestorId,
            Long categoriaId)
            throws HotelDuplicateException,
            GestorNotFoundException,
            CategoriaNotFoundException;
}
