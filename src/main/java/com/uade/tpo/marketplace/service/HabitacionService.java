package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entities.Habitacion;
import com.uade.tpo.marketplace.entities.dto.CarritoHabitacionDTO;
import com.uade.tpo.marketplace.entities.dto.HabitacionDTO;
import com.uade.tpo.marketplace.entities.dto.ReservaHabitacionDTO;
import com.uade.tpo.marketplace.enums.TipoHabitacion;
import com.uade.tpo.marketplace.exceptions.*;

public interface HabitacionService {
        public List<HabitacionDTO> getHabitaciones();

        public List<HabitacionDTO> getHabitacionesByHotel(String nombreHotel) throws HabitacionNotFoundException;

        public Optional<Habitacion> getHabitacionByNombreHotelAndNumeroHabitacion(String nombreHotel,
                        String numeroHabitacion)
                        throws HabitacionNotFoundException;

        public Habitacion createHabitacion(
                        TipoHabitacion tipoHabitacion,
                        int capacidad,
                        double precioPorNoche,
                        String numeroHabitacion,
                        List<Long> imagenes,
                        String username,
                        String categoria,
                        int ambientes,
                        int banos,
                        int dormitorios,
                        int camas,
                        String hotel,
                        List<ReservaHabitacionDTO> reservas,
                        List<CarritoHabitacionDTO> carritos) throws GestorNotFoundException, CategoriaNotFoundException,
                        HotelNotFoundException, HabitacionDuplicateException;

        public HabitacionDTO habitacionToHabitacionDTO(Habitacion habitacion);

        public void deleteHabitacion(String nombreHotel, String numeroHabitacion) throws HabitacionNotFoundException;

        public Habitacion updateHabitacion(String nombreHotel, String numeroHabitacion, HabitacionDTO habitacionRequest)
                        throws HabitacionNotFoundException, GestorNotFoundException, CategoriaNotFoundException,
                        ImagenNotFoundException;
}
