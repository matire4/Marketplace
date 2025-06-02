package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entities.Categoria;
import com.uade.tpo.marketplace.entities.Gestor;
import com.uade.tpo.marketplace.entities.Habitacion;
import com.uade.tpo.marketplace.entities.Hotel;
import com.uade.tpo.marketplace.entities.dto.HabitacionDTO;
import com.uade.tpo.marketplace.entities.dto.HotelDTO;
import com.uade.tpo.marketplace.enums.TipoHabitacion;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.exceptions.HotelDuplicateException;
import com.uade.tpo.marketplace.exceptions.HotelNotFoundException;
import com.uade.tpo.marketplace.repository.CategoriaRepository;
import com.uade.tpo.marketplace.repository.GestorRepository;
import com.uade.tpo.marketplace.repository.HotelRepository;

import jakarta.transaction.Transactional;

@Service
public class HabitacionServiceImpl implements HabitacionService{
    @Autowired
    private HabitacionRepository habitacionRepository;
    @Autowired
    private GestorRepository gestorRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<HabitacionDTO> getHabitaciones() {
            List<Habitacion> habitacion = habitacionRepository.findAll();
            return habitacion.stream().map(habitacion -> this.habitacionHabitacionDTO(habitacion)).toList();
    }

    public Optional<Habitacion> getHabitacionById(Long habitacionId)
                    throws HabitacionNotFoundException {
            return habitacionRepository.findById(habitacionId);
    }

    @Transactional
    public Habitacion createHabitacion(TipoHabitacion tipoHabitacion,
                        int capacidad, 
                        double precioPorNoche,
                        String numeroHabitacion,
                        String imagen,
                        Long gestorId,
                        Long categoriaId,
                        List<Long> reservasIds,
                        List<Long> carritosIds){

            List<Habitacion> habitacion = habitacionRepository.findByTipoHabitacion(TipoHabitacion tipoHabitacion);
            if (habitacion.isEmpty()) {

                    Gestor gestor = gestorRepository.findById(gestorId)
                                    .orElseThrow(() -> new GestorNotFoundException());
                    Categoria categoria = categoriaRepository.findById(categoriaId)
                                    .orElseThrow(() -> new CategoriaNotFoundException());

                    Hotel habitaciones = new Habitacion(
                                    tipoHabitacion,
                                    capacidad,
                                    precioPorNoche,
                                    numeroHabitacion,
                                    imagen,
                                    gestor,
                                    categoria,
                                    reservasIds,
                                    carritosIds);

                    if (habitaciones != null) {
                            habitaciones.forEach(habitacion -> {
                                    Habitacion newHabitacion = new Habitacion(
                                                    habitacion.getTipoHabitacion(),
                                                    habitacion.getCapacidad(),
                                                    habitacion.getPrecioPorNoche(),
                                                    habitacion.getNumeroHabitacion(),
                                                    habitacion.getImagen());
                                    newHabitacion.setHotel(hotel);
                                    hotel.getHabitaciones().add(newHabitacion);
                            });
                    }

                    return habitacionRepository.save(habitacion);
            }
            throw new HabitacionNotFoundException();
    }

    public HabitacionDTO habitacionHabitacionDTO(Habitacion habitacion) {
        HabitacionDTO habitacionDTO = new HabitacionDTO();

        habitacionDTO.setTipoHabitacion(habitacion.getTipoHabitacion());
        habitacionDTO.setCapacidad(habitacion.getCapacidad());
        habitacionDTO.setPrecioPorNoche(habitacion.getPrecioPorNoche());
        habitacionDTO.setNumeroHabitacion(habitacion.getNumeroHabitacion());
        habitacionDTO.setImagen(habitacion.getImagen());
        habitacionDTO.setGestorId(habitacion.getGestor().getId());
        habitacionDTO.setCategoriaId(habitacion.getCategoria().getId());

        return habitacionDTO;
    }

}
