package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entities.Categoria;
import com.uade.tpo.marketplace.entities.dto.CategoriaDTO;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.repository.CategoriaRepository;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<CategoriaDTO> getCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream().map(this::categoriaToCategoriaDTO).toList();
    }

    @Override
    public Optional<Categoria> getCategoriaById(Long categoriaId) throws CategoriaNotFoundException {
        return categoriaRepository.findById(categoriaId);
    }   

    @Override
    public Categoria createCategoria(String nombre) {
        if (categoriaRepository.findByNombre(nombre).isPresent()) {
            throw new IllegalArgumentException("La categoría con el nombre '" + nombre + "' ya existe.");
        }
        Categoria categoria = new Categoria(nombre);
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria updateCategoria(Long categoriaId, String nombre) throws CategoriaNotFoundException {
    Optional<Categoria> optionalCategoria = categoriaRepository.findById(categoriaId);
    Categoria categoria = optionalCategoria.get();
    categoria.setNombre(nombre);
    return categoriaRepository.save(categoria);
    
}

@Override
public void deleteCategoria(Long categoriaId) throws CategoriaNotFoundException {
    Optional<Categoria> optionalCategoria = categoriaRepository.findById(categoriaId);
    categoriaRepository.delete(optionalCategoria.get());

}

    @Override
    public CategoriaDTO categoriaToCategoriaDTO(Categoria categoria) {
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(categoria.getId());
        categoriaDTO.setNombre(categoria.getNombre());
        if (categoria.getHoteles() != null) {
            categoriaDTO.setHotelesIds(categoria.getHoteles().stream().map(hotel -> hotel.getId()).toList());
        }
        return categoriaDTO;
    }

    @Override
    public Optional<Categoria> getCategoriaByNombre(String categoria) {
        return categoriaRepository.findByNombre(categoria);
    }
}