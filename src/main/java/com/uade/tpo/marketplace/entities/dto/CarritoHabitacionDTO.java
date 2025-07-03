package com.uade.tpo.marketplace.entities.dto;

import java.lang.classfile.instruction.ConstantInstruction.LoadConstantInstruction;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoHabitacionDTO {
    private Long id;
    private String nombreReserva;
    private int cantidad;
    private Date checkIn;
    private Date checkOut;
    private Long hotelId; 
    private Long habitacionId;
    private Long carritoId;
    private Double precio;
    private String tipo = "habitacion"; 
    //
    public CarritoHabitacionDTO(Long id, String nombreReserva, int cantidad, Date checkIn, Date checkOut, Long hotelId, Long habitacionId, Long carritoId, Double precio) {
        this.id = id;
        this.nombreReserva = nombreReserva;
        this.cantidad = cantidad;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.hotelId = hotelId;
        this.habitacionId = habitacionId;
        this.carritoId = carritoId;
        this.precio = precio;
    }
}
