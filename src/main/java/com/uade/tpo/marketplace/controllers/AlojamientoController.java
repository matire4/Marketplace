package com.uade.tpo.marketplace.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.marketplace.entities.Gestor;
import com.uade.tpo.marketplace.entities.dto.AlojamientoDTO;
import com.uade.tpo.marketplace.exceptions.AlojamientoNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.service.AlojamientoService;

@RestController
@RequestMapping("/api/v1/alojamientos")
public class AlojamientoController {

    @Autowired
    private AlojamientoService alojamientoService;

    @GetMapping
    public ResponseEntity<List<AlojamientoDTO>> getAlojamientos() {
        return ResponseEntity.ok(alojamientoService.getAlojamientos());
    }

    @GetMapping("/{alojamientoId}")
    public ResponseEntity<AlojamientoDTO> getAlojamientoById(@PathVariable Long alojamientoId)
            throws AlojamientoNotFoundException {
        return ResponseEntity.ok(alojamientoService.getAlojamientoById(alojamientoId)
                .map(alojamientoService::alojamientoToAlojamientoDTO)
                .orElseThrow(() -> new AlojamientoNotFoundException("Alojamiento no encontrado")));
    }

    @GetMapping("/gestor/{usuario}")
    public ResponseEntity<List<AlojamientoDTO>> getAlojamientosByGestor(@PathVariable String usuario) throws GestorNotFoundException {
        return ResponseEntity.ok(alojamientoService.getAlojamientosByGestor(usuario));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<AlojamientoDTO>> getAlojamientosByCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(alojamientoService.getAlojamientosByCategoria(categoriaId));
    }
}