package com.uade.tpo.marketplace.entities.dto;

import com.uade.tpo.marketplace.entities.Habitacion;

import lombok.Data;

@Data
public class CarritoHabitacionDTO {
    private Long id;
    private Habitacion habitacion;
    private CarritoDTO carrito;
}
