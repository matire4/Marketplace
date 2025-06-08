package com.uade.tpo.marketplace.entities.dto;

import java.sql.Date;

import com.uade.tpo.marketplace.enums.Estado;

import lombok.Data;

@Data
public class ReservaHabitacionDTO {
    private Long habitacionId;
    private Long reservaId;
    private String nombreReserva;
    private Date fechaDesde;
    private Date fechaHasta;
    private double precio;
    private int cantidadPersonas;
    private Estado estado;
}
