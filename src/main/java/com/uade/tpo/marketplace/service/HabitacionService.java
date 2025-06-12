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

        public Optional<Habitacion> getHabitacionById(long habitacionId)
                        throws HabitacionNotFoundException;

        public Habitacion createHabitacion(TipoHabitacion tipoHabitacion,
                        int capacidad, 
                        double precioPorNoche,
                        String numeroHabitacion,
                        List<Long> imagenes,
                        Long gestorId,
                        Long categoriaId) throws GestorNotFoundException, CategoriaNotFoundException;

        public HabitacionDTO habitacionToHabitacionDTO(Habitacion habitacion);

        public void deleteHabitacion(Long habitacionId) throws HabitacionNotFoundException;

        public Habitacion updateHabitacion(Long habitacionId, HabitacionDTO habitacionRequest)
                        throws HabitacionNotFoundException, GestorNotFoundException, CategoriaNotFoundException, ImagenNotFoundException;
}
