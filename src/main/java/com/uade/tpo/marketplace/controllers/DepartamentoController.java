package com.uade.tpo.marketplace.controllers;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DepartamentoDTO> createDepartamento(@ModelAttribute DepartamentoDTO departamentoRequest)
            throws GestorNotFoundException, CategoriaNotFoundException, IOException {
        Departamento result = departamentoService.createDepartamento(
            departamentoRequest.getCapacidad(),
            departamentoRequest.getPrecioPorNoche(),
            departamentoRequest.getNumeroDepartamento(),
            departamentoRequest.getCiudad(),
            departamentoRequest.getPais(),
            departamentoRequest.getBreveDescripcion(),
            departamentoRequest.getDescripcion(),
            departamentoRequest.getDireccion(),
            departamentoRequest.getAmbientes(),
            departamentoRequest.getBanos(),
            departamentoRequest.getDormitorios(),
            departamentoRequest.getCamas(),
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

    @PutMapping(value = "/{departamentoId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DepartamentoDTO> updateDepartamento(@PathVariable Long departamentoId,
            @ModelAttribute DepartamentoDTO departamentoRequest) throws DepartamentoNotFoundException,
            GestorNotFoundException, CategoriaNotFoundException, IOException {
            System.out.println("Updating departamento with ID: " + departamentoId);
            System.out.println("Request data: " + departamentoRequest);
        var result = departamentoService.updateDepartamento(departamentoId, departamentoRequest);
        return ResponseEntity.ok(departamentoService.departamentoToDepartamentoDTO(result));
    }
}