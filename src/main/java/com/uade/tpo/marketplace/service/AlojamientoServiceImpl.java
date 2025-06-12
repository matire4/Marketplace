package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entities.Alojamiento;
import com.uade.tpo.marketplace.entities.dto.AlojamientoDTO;
import com.uade.tpo.marketplace.exceptions.AlojamientoNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.repository.AlojamientoRepository;

@Service
public class AlojamientoServiceImpl implements AlojamientoService {

    @Autowired
    private AlojamientoRepository alojamientoRepository;
    @Autowired
    private GestorService gestorService;

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

    @Override
    public AlojamientoDTO alojamientoToAlojamientoDTO(Alojamiento alojamiento) {
        return AlojamientoDTO.builder()
                .id(alojamiento.getId())
                .descripcion(alojamiento.getDescripcion())
                .direccion(alojamiento.getDireccion())
                .ciudad(alojamiento.getCiudad())
                .pais(alojamiento.getPais())
                .gestorId(alojamiento.getGestor().getId())
                .categoriaId(alojamiento.getCategoria().getId())
                .reviews(alojamiento.getReviews().stream().map(review -> review.getId()).toList())
                .preguntas(alojamiento.getPreguntas().stream().map(pregunta -> pregunta.getId()).toList())
                .imagenes(alojamiento.getImagenes().stream().map(imagen -> imagen.getId()).toList())
                .build();
    }

    @Override
    public List<AlojamientoDTO> getAlojamientosByGestor(String usuario) throws GestorNotFoundException {
        return alojamientoRepository.findByGestor(gestorService.getGestorByUsername(usuario).get()).stream()
                .map(this::alojamientoToAlojamientoDTO)
                .toList();
    }
}