package com.uade.tpo.marketplace.entities.dto;

import lombok.Data;

@Data
public class CuponDTO {
    private Long id;
    private String codigo;
    private double descuento;
}
