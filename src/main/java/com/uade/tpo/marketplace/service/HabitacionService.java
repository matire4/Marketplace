package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entities.Habitacion;
import com.uade.tpo.marketplace.entities.dto.HabitacionDTO;
import com.uade.tpo.marketplace.enums.TipoHabitacion;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.exceptions.ImagenNotFoundException;

public interface HabitacionService {
        public List<HabitacionDTO> getHabitaciones();

        public List<HabitacionDTO> getHabitacionesByHotel(String nombreHotel) throws HabitacionNotFoundException;

        public Optional<Habitacion> getHabitacionByNombreHotelAndNumeroHabitacion(String nombreHotel, String numeroHabitacion)
                        throws HabitacionNotFoundException;

        public Habitacion createHabitacion(TipoHabitacion tipoHabitacion,
                        int capacidad, 
                        double precioPorNoche,
                        String numeroHabitacion,
                        List<Long> imagenes,
                        String username,
                        String categoria) throws GestorNotFoundException, CategoriaNotFoundException;

        public HabitacionDTO habitacionToHabitacionDTO(Habitacion habitacion);

        public void deleteHabitacion(String nombreHotel, String numeroHabitacion) throws HabitacionNotFoundException;

        public Habitacion updateHabitacion(String nombreHotel, String numeroHabitacion, HabitacionDTO habitacionRequest)
                        throws HabitacionNotFoundException, GestorNotFoundException, CategoriaNotFoundException, ImagenNotFoundException;
}
