package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import org.springframework.transaction.annotation.Transactional;
import com.uade.tpo.marketplace.entities.Hotel;
import com.uade.tpo.marketplace.entities.Departamento;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.lang.System;

import com.uade.tpo.marketplace.entities.Alojamiento;
import com.uade.tpo.marketplace.entities.dto.AlojamientoDTO;
import com.uade.tpo.marketplace.entities.dto.DepartamentoDTO;
import com.uade.tpo.marketplace.entities.dto.HotelDTO;
import com.uade.tpo.marketplace.exceptions.AlojamientoNotFoundException;
import com.uade.tpo.marketplace.exceptions.DepartamentoNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.HotelNotFoundException;
import com.uade.tpo.marketplace.repository.AlojamientoRepository;
import com.uade.tpo.marketplace.repository.ImagenRepository;

@Service
public class AlojamientoServiceImpl implements AlojamientoService {

    @Autowired
    private AlojamientoRepository alojamientoRepository;
    @Autowired
    private GestorService gestorService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private HabitacionService habitacionService;
    @Autowired
    private ImagenRepository imagenRepository;

    @Override
    public List<AlojamientoDTO> getAlojamientos() {
        return alojamientoRepository.findAll().stream()
                .map(this::alojamientoToAlojamientoDTO)
                .toList();
    }

    @Override
    public Optional<Alojamiento> getAlojamientoById(Long alojamientoId) throws AlojamientoNotFoundException {
        return alojamientoRepository.findById(alojamientoId);
    }

    @Override
    public List<AlojamientoDTO> getAlojamientosByCiudad(String ciudad) {
        return alojamientoRepository.findByCiudad(ciudad).stream()
                .map(this::alojamientoToAlojamientoDTO)
                .toList();
    }

    @Override
    public List<AlojamientoDTO> getAlojamientosByCategoria(Long categoriaId) {
        return alojamientoRepository.findByCategoriaId(categoriaId).stream()
                .map(this::alojamientoToAlojamientoDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public AlojamientoDTO alojamientoToAlojamientoDTO(Alojamiento alojamiento) {
        if (alojamiento == null) {
            return null;
        }
        String tipo = (alojamiento instanceof Hotel) ? "hotel"
                : (alojamiento instanceof Departamento) ? "departamento" : "";
        AlojamientoDTO.AlojamientoDTOBuilder builder = AlojamientoDTO.builder()
                .tipoAlojamiento(tipo)
                .id(alojamiento.getId())
                .descripcion(alojamiento.getDescripcion())
                .direccion(alojamiento.getDireccion())
                .ciudad(alojamiento.getCiudad())
                .pais(alojamiento.getPais());
        if (tipo == "hotel") {
            Hotel h = hotelService.findById(alojamiento.getId()).get();
            OptionalDouble minPrecio = h.getHabitaciones().stream()
                .mapToDouble(hab -> hab.getPrecioPorNoche())
                .min();
                
            if (minPrecio.isPresent()) {
                builder.precio(minPrecio.getAsDouble());
            }
        }else if (tipo == "departamento") {
            Departamento d = (Departamento) alojamiento;
            builder.precio(d.getPrecioPorNoche());
        }
        if (alojamiento.getGestor() != null) {
            builder.gestorId(alojamiento.getGestor().getId());
        }
        if (alojamiento.getCategoria() != null) {
            builder.categoriaId(alojamiento.getCategoria().getId());
        }
        if (alojamiento.getReviews() != null && Hibernate.isInitialized(alojamiento.getReviews())) {
            builder.reviews(alojamiento.getReviews().stream()
                    .map(review -> review.getId())
                    .toList());
        }
        if (alojamiento.getPreguntas() != null && Hibernate.isInitialized(alojamiento.getPreguntas())) {
            builder.preguntas(alojamiento.getPreguntas().stream()
                    .map(pregunta -> pregunta.getId())
                    .toList());
        }
        builder.imagenesIds(imagenRepository.findByAlojamientoId(alojamiento.getId()).stream()
                .map(i -> i.getId())
                .toList());
        return builder.build();

    }

    @Override
    public List<AlojamientoDTO> getAlojamientosByGestor(String usuario) throws GestorNotFoundException {
        return alojamientoRepository.findByGestor(gestorService.getGestorByUsername(usuario).get()).stream()
                .map(this::alojamientoToAlojamientoDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HotelDTO getHotelById(Long alojamientoId) throws HotelNotFoundException {
        Alojamiento alojamiento = alojamientoRepository.findById(alojamientoId)
                .orElseThrow(() -> new HotelNotFoundException());

        if (!(alojamiento instanceof Hotel)) {
            throw new IllegalArgumentException("El alojamiento no es un hotel");
        }

        Hotel hotel = (Hotel) alojamiento;
        HotelDTO hotelDTO = new HotelDTO();

        hotelDTO.setId(hotel.getId());
        hotelDTO.setNombre(hotel.getNombre());
        hotelDTO.setTelefono(hotel.getTelefono());
        hotelDTO.setEmail(hotel.getEmail());
        hotelDTO.setDescripcion(hotel.getDescripcion());
        hotelDTO.setDireccion(hotel.getDireccion());
        hotelDTO.setCiudad(hotel.getCiudad());
        hotelDTO.setPais(hotel.getPais());

        if (hotel.getHabitaciones() != null) {
            hotelDTO.setHabitaciones(hotel.getHabitaciones().stream()
                    .map(h -> habitacionService.habitacionToHabitacionDTO(h))
                    .toList());
        }

        if (hotel.getImagenes() != null) {
            hotelDTO.setImagenesIds(hotel.getImagenes().stream()
                    .map(i -> i.getId())
                    .toList());
        }

        if (hotel.getGestor() != null) {
            hotelDTO.setUsername(hotel.getGestor().getUsername());
        }

        if (hotel.getCategoria() != null) {
            hotelDTO.setCategoria(hotel.getCategoria().getNombre());
        }

        return hotelDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public DepartamentoDTO getDepartamentoById(Long alojamientoId) throws DepartamentoNotFoundException {
        Alojamiento alojamiento = alojamientoRepository.findById(alojamientoId)
                .orElseThrow(() -> new DepartamentoNotFoundException());

        if (!(alojamiento instanceof Departamento)) {
            throw new IllegalArgumentException("El alojamiento no es un departamento");
        }

        Departamento departamento = (Departamento) alojamiento;
        DepartamentoDTO departamentoDTO = new DepartamentoDTO();

        departamentoDTO.setId(departamento.getId());
        departamentoDTO.setCapacidad(departamento.getCapacidad());
        departamentoDTO.setPrecioPorNoche(departamento.getPrecioPorNoche());
        departamentoDTO.setNumeroDepartamento(departamento.getNumeroDepartamento());
        departamentoDTO.setDescripcion(departamento.getDescripcion());
        departamentoDTO.setAmbientes(departamento.getAmbientes());
        departamentoDTO.setBanos(departamento.getBanos());
        departamentoDTO.setDormitorios(departamento.getDormitorios());
        departamentoDTO.setCamas(departamento.getCamas());
        departamentoDTO.setBreveDescripcion(departamento.getBreveDescripcion());
        departamentoDTO.setDireccion(departamento.getDireccion());
        departamentoDTO.setCiudad(departamento.getCiudad());
        departamentoDTO.setPais(departamento.getPais());

        if (departamento.getImagenes() != null) {
            departamentoDTO.setImagenesIds(departamento.getImagenes().stream()
                    .map(i -> i.getId())
                    .toList());
        }

        if (departamento.getGestor() != null) {
            departamentoDTO.setUsername(departamento.getGestor().getUsername());
        }

        if (departamento.getCategoria() != null) {
            departamentoDTO.setCategoria(departamento.getCategoria().getNombre());
        }

        return departamentoDTO;
    }
}