package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entities.Habitacion;
import com.uade.tpo.marketplace.entities.dto.HabitacionDTO;
import com.uade.tpo.marketplace.entities.dto.HotelDTO;
import com.uade.tpo.marketplace.enums.TipoHabitacion;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.repository.HabitacionRepository;

public interface HabitacionService {
        public List<HabitacionDTO> getHabitacion();

        public Optional<Habitacion> getHabitacionId(long habitacionesIds)
                        throws HabitacionNotFoundException;

        public Habitacion createHabitacion(TipoHabitacion tipoHabitacion,
                        int capacidad, 
                        double precioPorNoche,
                        String numeroHabitacion,
                        String imagen,
                        Long gestorId,
                        Long categoriaId,
                        List<Long> reservasIds,
                        List<Long> carritosIds,
                        Long gestorId,
                        Long categoriaId);

        public habitacion habitacionHabitacionDTO(Habitacion habitacion);


}
