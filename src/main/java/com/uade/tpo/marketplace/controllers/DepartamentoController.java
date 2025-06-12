package com.uade.tpo.marketplace.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entities.Departamento;
import com.uade.tpo.marketplace.entities.dto.DepartamentoDTO;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.exceptions.DepartamentoNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.service.DepartamentoService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/departamentos")
public class DepartamentoController {
    
    @Autowired
    private DepartamentoService departamentoService;

    @GetMapping
    public ResponseEntity<List<DepartamentoDTO>> getDepartamentos() {
        return ResponseEntity.ok(departamentoService.getDepartamentos());
    }

    @PostMapping
    public ResponseEntity<DepartamentoDTO> createDepartamento(@RequestBody DepartamentoDTO departamentoRequest)
            throws GestorNotFoundException, CategoriaNotFoundException {
        Departamento result = departamentoService.createDepartamento(
                departamentoRequest.getCapacidad(),
                departamentoRequest.getPrecioPorNoche(),
                departamentoRequest.getNumeroDepartamento(),
                departamentoRequest.getCiudad(),
                departamentoRequest.getPais(),
                departamentoRequest.getDescripcion(),
                departamentoRequest.getDireccion(),
                departamentoRequest.getImagenes(),
                departamentoRequest.getImagenesNuevas(),
                departamentoRequest.getUsername(),
                departamentoRequest.getCategoria());

        return ResponseEntity.created(URI.create("/api/v1/departamentos/" + result.getId()))
                .body(departamentoService.departamentoToDepartamentoDTO(result));
    }

    @DeleteMapping("/{departamentoId}")
    public ResponseEntity<Void> deleteDepartamento(@PathVariable Long departamentoId) throws DepartamentoNotFoundException {
        departamentoService.deleteDepartamento(departamentoId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{departamentoId}")
    public ResponseEntity<DepartamentoDTO> updateDepartamento(@PathVariable Long departamentoId,
            @RequestBody DepartamentoDTO departamentoRequest) throws DepartamentoNotFoundException,
            GestorNotFoundException, CategoriaNotFoundException {
        var result = departamentoService.updateDepartamento(departamentoId, departamentoRequest);
        return ResponseEntity.ok(departamentoService.departamentoToDepartamentoDTO(result));
    }
}