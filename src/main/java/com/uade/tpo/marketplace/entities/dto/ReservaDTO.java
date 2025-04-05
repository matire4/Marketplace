package com.uade.tpo.marketplace.entities.dto;

import java.sql.Date;

import com.uade.tpo.marketplace.enums.Estado;

import lombok.Data;

@Data
public class ReservaDTO {
    private Long id;
    private Date fecha;
    private double precio;
    private Estado estado;
    private Long habitacionId;
}