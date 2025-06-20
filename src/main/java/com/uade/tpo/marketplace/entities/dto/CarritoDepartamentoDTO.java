package com.uade.tpo.marketplace.entities.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoDepartamentoDTO {
    private Long id;
    private String nombreReserva;
    private int cantidad;
    private Date checkIn;
    private Date checkOut;
    private Long carritoId;
    private double precio;
    private Long departamentoId;
    private String tipo = "departamento"; 

    public CarritoDepartamentoDTO(Long id, String nombreReserva, int cantidad, Date checkIn, Date checkOut, Long carritoId, double precio, Long departamentoId) {
        this.id = id;
        this.nombreReserva = nombreReserva;
        this.cantidad = cantidad;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.carritoId = carritoId;
        this.precio = precio;
        this.departamentoId = departamentoId;
    }
}
