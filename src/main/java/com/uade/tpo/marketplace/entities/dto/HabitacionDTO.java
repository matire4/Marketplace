package com.uade.tpo.marketplace.entities.dto;

import java.util.List;

import com.uade.tpo.marketplace.enums.TipoHabitacion;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HabitacionDTO {
    private Long id;
    private TipoHabitacion tipoHabitacion;
    private int capacidad;
    private double precioPorNoche;
    private String numeroHabitacion;
    private int ambientes;
    private int banos;
    private int dormitorios;
    private int camas;
    private List<Long> imagenes;
    private String hotel;
    private List<ReservaHabitacionDTO> reservas;
    private List<CarritoHabitacionDTO> carritos;
    private String gestor;
    private String categoria;
}