package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entities.Imagen;
import com.uade.tpo.marketplace.entities.dto.ImagenDTO;
import com.uade.tpo.marketplace.exceptions.ImagenNotFoundException;

public interface ImagenService {
    List<ImagenDTO> getImagenes();
    
    Optional<Imagen> getImagenById(Long imagenId) throws ImagenNotFoundException;
    
    Imagen createImagen(
            String imagen,
            Long alojamientoId) throws ImagenNotFoundException;
    
    Imagen updateImagen(
            Long imagenId,
            String imagen) throws ImagenNotFoundException;

    void deleteImagen(Long imagenId) throws ImagenNotFoundException;
    
    ImagenDTO imagenToImagenDTO(Imagen imagen);

    List<ImagenDTO> getImagenesByAlojamientoId(Long alojamientoId);
    
    List<ImagenDTO> getImagenesByHabitacionId(Long habitacionId);
}