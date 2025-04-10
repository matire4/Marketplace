package com.uade.tpo.marketplace.controllers;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entities.Gestor;
import com.uade.tpo.marketplace.entities.dto.GestorDTO;
import com.uade.tpo.marketplace.exceptions.GestorDuplicateException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.service.GestorService;

@RestController
@RequestMapping("gestores")
public class GestorController {
    @Autowired
    private GestorService gestorService;

    @GetMapping("/{gestorId}")
    public ResponseEntity<GestorDTO> getGestorById(@PathVariable Long gestorId)
            throws GestorNotFoundException {
        Optional<Gestor> result = gestorService.getGestorById(gestorId);
        if (result.isPresent())
            return ResponseEntity.ok(gestorService.gestorToGestorDTO(result.get()));
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<GestorDTO> createGestor(@RequestBody GestorDTO gestorRequest)
            throws GestorDuplicateException {
        Gestor gestor = gestorService.createGestor(
                gestorRequest.getUsername(),
                gestorRequest.getPassword(),
                gestorRequest.getEmail(),
                gestorRequest.getTelefono(),
                gestorRequest.getNombre(),
                gestorRequest.getCuil());

        return ResponseEntity.created(URI.create("/gestores" + gestor.getId()))
                .body(gestorService.gestorToGestorDTO(gestor));
    }
}
