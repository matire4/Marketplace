package com.uade.tpo.marketplace.entities.dto;

import java.sql.Date;
import java.util.List;

import lombok.Data;

@Data
public class ReservaDTO {
    private Long id;
    private List<ReservaHabitacionDTO> habitaciones;
    private UsuarioDTO usuarioDTO;
    private Date fecha;
    private double precio;
}