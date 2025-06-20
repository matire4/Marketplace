package com.uade.tpo.marketplace.entities.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarritoHabitacionDTO {
    private Long id;
    private String nombreReserva;
    private int cantidad;
    private Date checkIn;
    private Date checkOut;
    private Long habitacionId;
    private Long carritoId;
    private Double precio;
}
