package com.uade.tpo.marketplace.entities.dto;

import java.util.List;

import com.uade.tpo.marketplace.enums.TipoHabitacion;

import lombok.Data;

@Data
public class HabitacionDTO {
    private TipoHabitacion tipoHabitacion;
    private int capacidad;
    private double precioPorNoche;
    private String numeroHabitacion;
    private int ambientes;
    private int banos;
    private int dormitorios;
    private int camas;
    private List<Long> imagenes;
    private Long hotelId;
    private List<Long> reservasIds;
    private List<Long> carritosIds;
    private Long gestorId;
    private Long categoriaId;
}