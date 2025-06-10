package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entities.Alojamiento;
import com.uade.tpo.marketplace.entities.Imagen;
import com.uade.tpo.marketplace.entities.dto.ImagenDTO;
import com.uade.tpo.marketplace.exceptions.ImagenNotFoundException;
import com.uade.tpo.marketplace.repository.AlojamientoRepository;
import com.uade.tpo.marketplace.repository.ImagenRepository;

import jakarta.transaction.Transactional;

@Service
public class ImagenServiceImpl implements ImagenService {
    
    @Autowired
    private ImagenRepository imagenRepository;
    
    @Autowired
    private AlojamientoRepository alojamientoRepository;

    @Override
    public List<ImagenDTO> getImagenes() {
        List<Imagen> imagenes = imagenRepository.findAll();
        return imagenes.stream()
                .map(this::imagenToImagenDTO)
                .toList();
    }

    @Override
    public Optional<Imagen> getImagenById(Long imagenId) throws ImagenNotFoundException {
        return Optional.ofNullable(imagenRepository.findById(imagenId)
                .orElseThrow(() -> new ImagenNotFoundException()));
    }

    @Override
    @Transactional
    public Imagen createImagen(String imagen, Long alojamientoId) throws ImagenNotFoundException {
        Alojamiento alojamiento = alojamientoRepository.findById(alojamientoId)
            .orElseThrow(() -> new ImagenNotFoundException());

        Imagen i = Imagen.builder()
            .imagen(imagen)
            .alojamiento(alojamiento)
            .build();

        return imagenRepository.save(i);
    }

    @Override
    public Imagen updateImagen(Long imagenId, String imagen) throws ImagenNotFoundException {
        Imagen i = imagenRepository.findById(imagenId)
                .orElseThrow(() -> new ImagenNotFoundException());

        i.setImagen(imagen);

        return imagenRepository.save(i);
    }

    @Override
    public void deleteImagen(Long imagenId) throws ImagenNotFoundException {
        if (!imagenRepository.existsById(imagenId)) {
            throw new ImagenNotFoundException();
        }
        imagenRepository.deleteById(imagenId);
    }

    @Override
    public ImagenDTO imagenToImagenDTO(Imagen imagen) {
        return ImagenDTO.builder()
                .id(imagen.getId())
                .imagen(imagen.getImagen())
                .alojamientoId(imagen.getAlojamiento().getId())
                .build();
    }

    @Override
    public List<ImagenDTO> getImagenesByAlojamientoId(Long alojamientoId) {
        List<Imagen> imagenes = imagenRepository.findByAlojamientoId(alojamientoId);
        return imagenes.stream()
                .map(this::imagenToImagenDTO)
                .toList();
    }
}