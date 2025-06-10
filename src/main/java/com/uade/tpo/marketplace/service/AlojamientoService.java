package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entities.Alojamiento;
import com.uade.tpo.marketplace.entities.dto.AlojamientoDTO;
import com.uade.tpo.marketplace.exceptions.AlojamientoNotFoundException;

public interface AlojamientoService {
    List<AlojamientoDTO> getAlojamientos();
    
    Optional<Alojamiento> getAlojamientoById(Long alojamientoId) throws AlojamientoNotFoundException;
    
    List<AlojamientoDTO> getAlojamientosByCiudad(String ciudad);
    
    List<AlojamientoDTO> getAlojamientosByCategoria(Long categoriaId);
    
    AlojamientoDTO alojamientoToAlojamientoDTO(Alojamiento alojamiento);
}