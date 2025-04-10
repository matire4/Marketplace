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
    private List<Long> habitacionesIds;

    private List<HabitacionDTO> habitacionesParaCrear;

    private List<Long> reviewsIds;
    private List<Long> preguntasIds;
    private Long gestorId;
    private Long categoriaId;
}