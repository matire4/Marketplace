package com.uade.tpo.marketplace.entities.dto;

import java.util.List;

import lombok.Data;

@Data
public class HotelDTO {
    private Long id;
    private String nombre;
    private String telefono;
    private String email;
    private String descripcion;
    private String direccion;
    private String ciudad;
    private String pais;

    private List<Long> habitaciones;
    private List<HabitacionDTO> habitacionesParaCrear;
    private List<Long> imagenes;

    private List<Long> reviews;
    private List<Long> preguntas;
    private String username;
    private String categoria;

    public void addHabitacion(HabitacionDTO h)
    {
        habitaciones.add(h.getId());
    }
}