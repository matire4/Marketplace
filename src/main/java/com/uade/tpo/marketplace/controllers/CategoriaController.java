package com.uade.tpo.marketplace.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.marketplace.entities.Categoria;
import com.uade.tpo.marketplace.entities.dto.CategoriaDTO;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.service.CategoriaService;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> getCategorias() {
        return ResponseEntity.ok(categoriaService.getCategorias());
    }

    @GetMapping("/{categoriaId}")
    public ResponseEntity<CategoriaDTO> getCategoriaById(@PathVariable Long categoriaId) throws CategoriaNotFoundException {
        Optional<Categoria> categoria = categoriaService.getCategoriaById(categoriaId);
        if (categoria.isPresent()) {
            return ResponseEntity.ok(categoriaService.categoriaToCategoriaDTO(categoria.get()));
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> createCategoria(@RequestBody CategoriaDTO categoriaDTO) {
        Categoria categoria = categoriaService.createCategoria(categoriaDTO.getNombre());
        return ResponseEntity.created(URI.create("/categorias/" + categoria.getId()))
                .body(categoriaService.categoriaToCategoriaDTO(categoria));
    }

    @PutMapping("/{categoriaId}")
    public ResponseEntity<CategoriaDTO> updateCategoria(@PathVariable Long categoriaId, @RequestBody CategoriaDTO categoriaDTO)
            throws CategoriaNotFoundException {
        Categoria updatedCategoria = categoriaService.updateCategoria(categoriaId, categoriaDTO.getNombre());
        return ResponseEntity.ok(categoriaService.categoriaToCategoriaDTO(updatedCategoria));
    }

    @DeleteMapping("/{categoriaId}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long categoriaId) throws CategoriaNotFoundException {
        categoriaService.deleteCategoria(categoriaId);
        return ResponseEntity.noContent().build();
    }
}