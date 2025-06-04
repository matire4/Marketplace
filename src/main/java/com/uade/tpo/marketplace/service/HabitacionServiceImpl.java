package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entities.Categoria;
import com.uade.tpo.marketplace.entities.Gestor;
import com.uade.tpo.marketplace.entities.Habitacion;
import com.uade.tpo.marketplace.entities.dto.HabitacionDTO;
import com.uade.tpo.marketplace.enums.TipoHabitacion;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.repository.CategoriaRepository;
import com.uade.tpo.marketplace.repository.GestorRepository;
import com.uade.tpo.marketplace.repository.HabitacionRepository;

import jakarta.transaction.Transactional;

@Service
public class HabitacionServiceImpl implements HabitacionService{
    @Autowired
    private HabitacionRepository habitacionRepository;
    @Autowired
    private GestorRepository gestorRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<HabitacionDTO> getHabitaciones() {
        List<Habitacion> habitaciones = habitacionRepository.findAll();
        return habitaciones.stream().map(habitacion -> this.habitacionToHabitacionDTO(habitacion)).toList();
    }

    @Override
    public Optional<Habitacion> getHabitacionById(long habitacionId) throws HabitacionNotFoundException {
        return habitacionRepository.findById(habitacionId);
    }

    @Transactional
    public Habitacion createHabitacion(TipoHabitacion tipoHabitacion,
                        int capacidad, 
                        double precioPorNoche,
                        String numeroHabitacion,
                        String imagen,
                        Long gestorId,
                        Long categoriaId) throws GestorNotFoundException, CategoriaNotFoundException{

        Gestor gestor = gestorRepository.findById(gestorId)
                        .orElseThrow(() -> new GestorNotFoundException());
        Categoria categoria = categoriaRepository.findById(categoriaId)
                        .orElseThrow(() -> new CategoriaNotFoundException());

        Habitacion habitacion = new Habitacion(
            tipoHabitacion,
            capacidad,
            precioPorNoche,
            numeroHabitacion,
            imagen,
            gestor,
            categoria);

        return habitacionRepository.save(habitacion);
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

    @Override
    public HabitacionDTO habitacionToHabitacionDTO(Habitacion habitacion) {
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

    @Override
    public void deleteHabitacion(Long habitacionId) throws HabitacionNotFoundException {
        if (!habitacionRepository.existsById(habitacionId)) {
            throw new HabitacionNotFoundException();
        }
        habitacionRepository.deleteById(habitacionId);
    }

    @Override
    public Habitacion updateHabitacion(Long habitacionId, HabitacionDTO habitacionRequest)
            throws HabitacionNotFoundException, GestorNotFoundException, CategoriaNotFoundException {
        Habitacion habitacion = habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new HabitacionNotFoundException());

        Gestor gestor = gestorRepository.findById(habitacionRequest.getGestorId())
                .orElseThrow(() -> new GestorNotFoundException());
        Categoria categoria = categoriaRepository.findById(habitacionRequest.getCategoriaId())
                .orElseThrow(() -> new CategoriaNotFoundException());

        habitacion.setTipoHabitacion(habitacionRequest.getTipoHabitacion());
        habitacion.setCapacidad(habitacionRequest.getCapacidad());
        habitacion.setPrecioPorNoche(habitacionRequest.getPrecioPorNoche());
        habitacion.setNumeroHabitacion(habitacionRequest.getNumeroHabitacion());
        habitacion.setImagen(habitacionRequest.getImagen());
        habitacion.setGestor(gestor);
        habitacion.setCategoria(categoria);

        return habitacionRepository.save(habitacion);
    }
}
