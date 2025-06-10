package com.uade.tpo.marketplace.entities;

import java.sql.Date;

import com.uade.tpo.marketplace.enums.Estado;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReservaDepartamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombreReserva;
    @Column(nullable = false)
    private Date fechaDesde;
    @Column(nullable = false)
    private Date fechaHasta;
    @Column(nullable = false)
    private Estado estado;
    @Column(nullable = false)
    private double precio;
    @Column(nullable = false)
    private int cantidadPersonas;

    @ManyToOne
    @JoinColumn(name = "reserva_id")
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;
    
    public ReservaDepartamento(String nombreReserva, Reserva reserva, Departamento departamento) {
        this.nombreReserva = nombreReserva;
        this.reserva = reserva;
        this.departamento = departamento;
    }
}
