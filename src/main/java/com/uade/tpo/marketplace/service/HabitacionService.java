package com.uade.tpo.marketplace.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

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
                        List<MultipartFile> imagenes,
                        String username,
                        String categoria,
                        int ambientes,
                        int banos,
                        int dormitorios,
                        int camas,
                        String hotel) throws GestorNotFoundException, CategoriaNotFoundException,
                        HotelNotFoundException, HabitacionDuplicateException, IOException;

        public HabitacionDTO habitacionToHabitacionDTO(Habitacion habitacion);

        public void deleteHabitacion(String nombreHotel, String numeroHabitacion) throws HabitacionNotFoundException;

        public Habitacion updateHabitacion(String nombreHotel, String numeroHabitacion, HabitacionDTO habitacionRequest, String gestor)
                        throws HabitacionNotFoundException, GestorNotFoundException, CategoriaNotFoundException, IOException,
                        ImagenNotFoundException;

        public Optional<Habitacion> getHabitacionById(Long habitacionId);
}
