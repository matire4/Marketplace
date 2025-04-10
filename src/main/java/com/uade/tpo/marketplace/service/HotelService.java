package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entities.Hotel;
import com.uade.tpo.marketplace.entities.dto.HotelDTO;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.HotelDuplicateException;
import com.uade.tpo.marketplace.exceptions.HotelNotFoundException;

public interface HotelService {
        public List<HotelDTO> getHotels();

        public Optional<Hotel> getHotelById(Long hotelId)
                        throws HotelNotFoundException;

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

        public HotelDTO hotelToHotelDTO(Hotel hotel);
}
