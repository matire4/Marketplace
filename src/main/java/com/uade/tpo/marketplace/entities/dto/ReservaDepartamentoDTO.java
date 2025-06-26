package com.uade.tpo.marketplace.entities.dto;

import java.sql.Date;

import com.uade.tpo.marketplace.enums.Estado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDepartamentoDTO {
    private Long id;
    private String nombreReserva;
    private Long reservaId;
    private Long departamentoId;
    private String departamentoNombre;
    private Date fechaDesde;
    private Date fechaHasta;
    private int cantidadPersonas;
    private double precio;
    private Estado estado;
}