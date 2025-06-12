package com.uade.tpo.marketplace.entities.dto;

import java.util.List;

import lombok.Data;

@Data
public class CarritoDTO {
    private Long id;
    private Long usuarioId;
    private List<HabitacionDTO> habitaciones;
    private List<DepartamentoDTO> departamentos;
}