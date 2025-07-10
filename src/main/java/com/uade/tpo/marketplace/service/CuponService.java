package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entities.Cupon;
import com.uade.tpo.marketplace.entities.dto.CuponDTO;
import com.uade.tpo.marketplace.exceptions.CuponNotFoundException;

public interface CuponService {
    List<CuponDTO> getCupones();

    Optional<Cupon> getCuponById(Long cuponId) throws CuponNotFoundException;

    Optional<Cupon> getCuponByCodigo(String codigo) throws CuponNotFoundException;

    Cupon createCupon(String codigo, double descuento);

    void deleteCupon(Long cuponId) throws CuponNotFoundException;

    CuponDTO cuponToCuponDTO(Cupon cupon);
}
