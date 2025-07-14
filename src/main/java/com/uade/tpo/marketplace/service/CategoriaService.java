package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entities.Categoria;
import com.uade.tpo.marketplace.entities.Departamento;
import com.uade.tpo.marketplace.entities.dto.CategoriaDTO;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;

public interface CategoriaService {
    List<CategoriaDTO> getCategorias();

    Optional<Categoria> getCategoriaById(Long categoriaId) throws CategoriaNotFoundException;

    Categoria createCategoria(String nombre);

    Categoria updateCategoria(Long categoriaId, String nombre) throws CategoriaNotFoundException;

    void deleteCategoria(Long categoriaId) throws CategoriaNotFoundException;

    CategoriaDTO categoriaToCategoriaDTO(Categoria categoria);

    Optional<Categoria> getCategoriaByNombre(String categoria);
}