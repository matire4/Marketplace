package com.uade.tpo.marketplace.service;

import java.sql.Date;
import java.util.List;

import com.uade.tpo.marketplace.entities.CarritoDepartamento;
import com.uade.tpo.marketplace.entities.dto.CarritoDepartamentoDTO;
import com.uade.tpo.marketplace.exceptions.CarritoNotFoundException;
import com.uade.tpo.marketplace.exceptions.DepartamentoNotFoundException;


public interface CarritoDepartamentoService {

    List<CarritoDepartamentoDTO> getCarritosDepartamento();

    CarritoDepartamentoDTO createCarritoDepartamento(
            Long carritoId,
            Long departamentoId,
            String nombreReserva,
            int cantidad,
            Date checkIn,
            Date checkOut,
            Double precio) throws CarritoNotFoundException, DepartamentoNotFoundException;

    void deleteCarritoDepartamento(Long id) throws CarritoNotFoundException;

    List<CarritoDepartamentoDTO> getCarritosDepartamentosByCarritoId(Long carritoId);

    List<CarritoDepartamentoDTO> getCarritosDepartamentosByDepartamentoId(Long departamentoId);

    CarritoDepartamentoDTO carritoDepartamentoDTO(CarritoDepartamento carritoDepartamento);

    CarritoDepartamentoDTO save(CarritoDepartamento carritoDepartamento);

}
