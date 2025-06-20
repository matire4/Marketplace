package com.uade.tpo.marketplace.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoDepartamentoDTO {
    private Long id;
    private String nombreReserva;
    private int cantidad;
    private String checkIn;
    private String checkOut;
    private Long carritoId;
    private double precio;
    private Long departamentoId;
}
