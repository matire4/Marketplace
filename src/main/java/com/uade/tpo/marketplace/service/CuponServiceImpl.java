package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entities.Cupon;
import com.uade.tpo.marketplace.entities.dto.CuponDTO;
import com.uade.tpo.marketplace.exceptions.CuponNotFoundException;
import com.uade.tpo.marketplace.repository.CuponRepository;

@Service
public class CuponServiceImpl implements CuponService {

    @Autowired
    private CuponRepository cuponRepository;

    @Override
    public List<CuponDTO> getCupones() {
        List<Cupon> cupones = cuponRepository.findAll();
        return cupones.stream().map(this::cuponToCuponDTO).toList();
    }

    @Override
    public Optional<Cupon> getCuponById(Long cuponId) throws CuponNotFoundException {
        return cuponRepository.findById(cuponId);
    }

    @Override
    public Optional<Cupon> getCuponByCodigo(String codigo) throws CuponNotFoundException {
        return cuponRepository.findByCodigo(codigo);
    }

    @Override
    public Cupon createCupon(String codigo, double descuento) {
        if (cuponRepository.findByCodigo(codigo).isPresent()) {
            throw new IllegalArgumentException("El cupón con el código '" + codigo + "' ya existe.");
        }
        Cupon cupon = new Cupon(codigo, descuento);
        return cuponRepository.save(cupon);
    }

    @Override
    public void deleteCupon(Long cuponId) throws CuponNotFoundException {
        Optional<Cupon> optionalCupon = cuponRepository.findById(cuponId);
        if (optionalCupon.isEmpty()) {
            throw new CuponNotFoundException();
        }
        cuponRepository.delete(optionalCupon.get());
    }

    @Override
    public CuponDTO cuponToCuponDTO(Cupon cupon) {
        CuponDTO cuponDTO = new CuponDTO();
        cuponDTO.setId(cupon.getId());
        cuponDTO.setCodigo(cupon.getCodigo());
        cuponDTO.setDescuento(cupon.getDescuento());
        return cuponDTO;
    }
}
