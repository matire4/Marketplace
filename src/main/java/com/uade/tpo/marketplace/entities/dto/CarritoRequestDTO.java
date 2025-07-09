package com.uade.tpo.marketplace.entities.dto;

import lombok.Data;


@Data
public class CarritoRequestDTO {
    private String titularReserva;
    private String nombreReserva;
    private String checkIn;
    private String checkOut;
    private int cantidad;
    private double precio;
}