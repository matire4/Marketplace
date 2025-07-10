package com.uade.tpo.marketplace.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.marketplace.entities.Cupon;
import com.uade.tpo.marketplace.entities.dto.CuponDTO;
import com.uade.tpo.marketplace.exceptions.CuponNotFoundException;
import com.uade.tpo.marketplace.service.CuponService;

@RestController
@RequestMapping("/api/v1/cupones")
public class CuponController {

    @Autowired
    private CuponService cuponService;

    @GetMapping
    public ResponseEntity<List<CuponDTO>> getCupones() {
        return ResponseEntity.ok(cuponService.getCupones());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Double> getCuponByCodigo(@PathVariable String codigo) throws CuponNotFoundException {
        Optional<Cupon> cupon = cuponService.getCuponByCodigo(codigo);
        if (cupon.isPresent()) {
            return ResponseEntity.ok(cupon.get().getDescuento());
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<CuponDTO> createCupon(@RequestBody CuponDTO cuponDTO) {
        Cupon cupon = cuponService.createCupon(cuponDTO.getCodigo(), cuponDTO.getDescuento());
        return ResponseEntity.created(URI.create("/cupones/" + cupon.getId()))
                .body(cuponService.cuponToCuponDTO(cupon));
    }

    @DeleteMapping("/{cuponId}")
    public ResponseEntity<Void> deleteCupon(@PathVariable Long cuponId) throws CuponNotFoundException {
        cuponService.deleteCupon(cuponId);
        return ResponseEntity.noContent().build();
    }
}
