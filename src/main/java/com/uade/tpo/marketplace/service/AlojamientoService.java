package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entities.Alojamiento;
import com.uade.tpo.marketplace.entities.dto.AlojamientoDTO;
import com.uade.tpo.marketplace.exceptions.AlojamientoNotFoundException;
import com.uade.tpo.marketplace.exceptions.DepartamentoNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.HotelDuplicateException;
import com.uade.tpo.marketplace.exceptions.HotelNotFoundException;

public interface AlojamientoService {
    List<AlojamientoDTO> getAlojamientos();
    
    Optional<Alojamiento> getAlojamientoById(Long alojamientoId) throws AlojamientoNotFoundException;
    
    List<AlojamientoDTO> getAlojamientosByCiudad(String ciudad);
    
    List<AlojamientoDTO> getAlojamientosByCategoria(Long categoriaId);

    List<AlojamientoDTO> getAlojamientosByGestor(String usuario) throws GestorNotFoundException;
    
    AlojamientoDTO alojamientoToAlojamientoDTO(Alojamiento alojamiento);

    Object getHotelById(Long alojamientoId) throws HotelNotFoundException;

    Object getDepartamentoById(Long alojamientoId)throws DepartamentoNotFoundException;
}