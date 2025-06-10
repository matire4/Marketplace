package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entities.Departamento;
import com.uade.tpo.marketplace.entities.dto.DepartamentoDTO;
import com.uade.tpo.marketplace.entities.dto.ImagenDTO;
import com.uade.tpo.marketplace.exceptions.DepartamentoNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;

public interface DepartamentoService {
    List<DepartamentoDTO> getDepartamentos();
    
    Optional<Departamento> getDepartamentoById(Long departamentoId) throws DepartamentoNotFoundException;
    
    Departamento createDepartamento(
            int capacidad,
            double precioPorNoche,
            String numeroDepartamento,
            String ciudad,
            String pais,
            String descripcion,
            String direccion,
            List<Long> imagenes,
            List<ImagenDTO> imagenCrear,
            Long gestorId,
            Long categoriaId) throws GestorNotFoundException, CategoriaNotFoundException;
    
    Departamento updateDepartamento(
            Long departamentoId,
            DepartamentoDTO departamentoDTO) throws DepartamentoNotFoundException, GestorNotFoundException, CategoriaNotFoundException;
    
    void deleteDepartamento(Long departamentoId) throws DepartamentoNotFoundException;
    
    DepartamentoDTO departamentoToDepartamentoDTO(Departamento departamento);
}