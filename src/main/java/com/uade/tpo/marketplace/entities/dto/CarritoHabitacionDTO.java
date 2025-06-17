package com.uade.tpo.marketplace.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoHabitacionDTO {
    private Long id;
    private String nombreReserva;
    private HabitacionDTO habitacion;
    private CarritoDTO carrito;
}
