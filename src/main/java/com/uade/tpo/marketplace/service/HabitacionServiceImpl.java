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
import com.uade.tpo.marketplace.enums.TipoHabitacion;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.HabitacionNotFoundException;
import com.uade.tpo.marketplace.exceptions.ImagenNotFoundException;
import com.uade.tpo.marketplace.repository.CategoriaRepository;
import com.uade.tpo.marketplace.repository.GestorRepository;
import com.uade.tpo.marketplace.repository.HabitacionRepository;
import com.uade.tpo.marketplace.repository.HotelRepository;
import com.uade.tpo.marketplace.repository.ImagenRepository;

import jakarta.transaction.Transactional;

@Service
public class HabitacionServiceImpl implements HabitacionService{
    @Autowired
    private HabitacionRepository habitacionRepository;
    @Autowired
    private GestorRepository gestorRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ImagenRepository imagenRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    GestorService gestorService;
    @Autowired
    CategoriaService categoriaService;

    @Override
    public List<HabitacionDTO> getHabitaciones() {
        List<Habitacion> habitaciones = habitacionRepository.findAll();
        return habitaciones.stream().map(habitacion -> this.habitacionToHabitacionDTO(habitacion)).toList();
    }

    @Override
    public Optional<Habitacion> getHabitacionByNombreHotelAndNumeroHabitacion(String nombreHotel, String numeroHabitacion) throws HabitacionNotFoundException {
        Hotel hotel = hotelRepository.findByNombre(nombreHotel)
                .orElseThrow(() -> new HabitacionNotFoundException());
        return habitacionRepository.findByHotelAndNumeroHabitacion(hotel, numeroHabitacion);
    }

    @Transactional
    public Habitacion createHabitacion(TipoHabitacion tipoHabitacion,
                        int capacidad, 
                        double precioPorNoche,
                        String numeroHabitacion,
                        List<Long> imagenes,
                        String username,
                        String categoria) throws GestorNotFoundException, CategoriaNotFoundException{

        Gestor gestor = gestorRepository.findByUsername(username)
                        .orElseThrow(() -> new GestorNotFoundException());
        Categoria c = categoriaRepository.findByNombre(categoria)
                        .orElseThrow(() -> new CategoriaNotFoundException());

        Habitacion habitacion = new Habitacion(
            tipoHabitacion,
            capacidad,
            precioPorNoche,
            numeroHabitacion,
            imagenRepository.findAllById(imagenes),
            gestor,
            c);

        return habitacionRepository.save(habitacion);
    }

    @Override
    public HabitacionDTO habitacionToHabitacionDTO(Habitacion habitacion) {
        HabitacionDTO habitacionDTO = new HabitacionDTO();

        habitacionDTO.setTipoHabitacion(habitacion.getTipoHabitacion());
        habitacionDTO.setCapacidad(habitacion.getCapacidad());
        habitacionDTO.setPrecioPorNoche(habitacion.getPrecioPorNoche());
        habitacionDTO.setNumeroHabitacion(habitacion.getNumeroHabitacion());
        habitacionDTO.setImagenes(habitacion.getImagenesHabitacion().stream()
                .map(imagen -> imagen.getId())
                .toList());
        habitacionDTO.setGestor(gestorService.gestorToGestorDTO(habitacion.getGestor()));
        habitacionDTO.setCategoria(categoriaService.categoriaToCategoriaDTO(habitacion.getCategoria()));

        return habitacionDTO;
    }

    @Override
    public void deleteHabitacion(String nombreHotel, String numeroHabitacion) throws HabitacionNotFoundException {
        Habitacion habitacion = habitacionRepository.findByHotelAndNumeroHabitacion(hotelRepository.findByNombre(nombreHotel)
                .orElseThrow(() -> new HabitacionNotFoundException()), numeroHabitacion)
                .orElseThrow(() -> new HabitacionNotFoundException());
        habitacionRepository.delete(habitacion);
    }

    @Override
    public Habitacion updateHabitacion(String nombreHotel, String numeroHabitacion, HabitacionDTO habitacionRequest)
            throws HabitacionNotFoundException, GestorNotFoundException, CategoriaNotFoundException , ImagenNotFoundException {

        Habitacion habitacion = habitacionRepository.findByHotelAndNumeroHabitacion(hotelRepository.findByNombre(nombreHotel)
                .orElseThrow(() -> new HabitacionNotFoundException()), numeroHabitacion)
                .orElseThrow(() -> new HabitacionNotFoundException());
        Gestor gestor = gestorRepository.findById(habitacionRequest.getGestor().getId())
                .orElseThrow(() -> new GestorNotFoundException());
        Categoria categoria = categoriaRepository.findById(habitacionRequest.getCategoria().getId())
                .orElseThrow(() -> new CategoriaNotFoundException());
        habitacion.setTipoHabitacion(habitacionRequest.getTipoHabitacion());
        habitacion.setCapacidad(habitacionRequest.getCapacidad());
        habitacion.setPrecioPorNoche(habitacionRequest.getPrecioPorNoche());
        habitacion.setNumeroHabitacion(habitacionRequest.getNumeroHabitacion());
        habitacion.setImagenesHabitacion(imagenRepository.findAllById(habitacionRequest.getImagenes()));
        habitacion.setGestor(gestor);
        habitacion.setCategoria(categoria);

        return habitacionRepository.save(habitacion);
    }

    @Override
    public List<HabitacionDTO> getHabitacionesByHotel(String nombreHotel) throws HabitacionNotFoundException {
        Hotel hotel = hotelRepository.findByNombre(nombreHotel)
                .orElseThrow(() -> new HabitacionNotFoundException());
        List<Habitacion> habitaciones = habitacionRepository.findByHotel(hotel);
        return habitaciones.stream().map(habitacion -> this.habitacionToHabitacionDTO(habitacion)).toList();
    }
}
