package com.uade.tpo.marketplace.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartamentoDTO {
    private Long id;
    private int capacidad;
    private double precioPorNoche;
    private String numeroDepartamento;
    private String descripcion;
    private String direccion;
    private String imagen;
    private Long gestorId;
    private Long categoriaId;
}