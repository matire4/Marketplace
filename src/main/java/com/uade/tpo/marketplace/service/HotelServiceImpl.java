package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entities.Categoria;
import com.uade.tpo.marketplace.entities.Gestor;
import com.uade.tpo.marketplace.entities.Hotel;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.HotelDuplicateException;
import com.uade.tpo.marketplace.exceptions.HotelNotFoundException;
import com.uade.tpo.marketplace.repository.CategoriaRepository;
import com.uade.tpo.marketplace.repository.GestorRepository;
import com.uade.tpo.marketplace.repository.HotelRepository;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private GestorRepository gestorRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Hotel> getHotels() {
        return hotelRepository.findAll();
    }

    public Optional<Hotel> getHotelById(Long hotelId)
            throws HotelNotFoundException {
        return hotelRepository.findById(hotelId);
    }

    public Hotel createHotel(String nombre,
            String telefono,
            String email,
            String description,
            String direccion,
            String ciudad,
            String pais,
            Long gestorId,
            Long categoriaId) throws HotelDuplicateException, GestorNotFoundException,
            CategoriaNotFoundException {

        List<Hotel> hoteles = hotelRepository.findByEmail(email);
        if (hoteles.isEmpty()) {

            Gestor gestor = gestorRepository.findById(gestorId)
                    .orElseThrow(() -> new GestorNotFoundException());
            Categoria categoria = categoriaRepository.findById(categoriaId)
                    .orElseThrow(() -> new CategoriaNotFoundException());

            return hotelRepository.save(new Hotel(
                    nombre,
                    telefono,
                    email,
                    description,
                    direccion,
                    ciudad,
                    pais,
                    gestor,
                    categoria));
        }
        throw new HotelDuplicateException();
    }
}
