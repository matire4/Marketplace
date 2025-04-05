package com.uade.tpo.marketplace.entities.dto;

import java.util.List;

import com.uade.tpo.marketplace.enums.TipoHabitacion;

import lombok.Data;

@Data
public class HabitacionDTO {
    private Long id;
    private TipoHabitacion tipoHabitacion;
    private int capacidad;
    private double precioPorNoche;
    private String numeroHabitacion;
    private String imagen;
    private Long hotelId;
    private List<Long> reservasIds;
    private List<Long> carritosIds;
}