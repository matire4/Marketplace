package com.uade.tpo.marketplace.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class ReservaHabitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombreReserva;

    @ManyToOne
    @JoinColumn(name = "reserva_id")
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "habitacion_id")
    private Habitacion habitacion;

    public ReservaHabitacion() {
    }
    
    public ReservaHabitacion(String nombreReserva, Reserva reserva, Habitacion habitacion) {
        this.nombreReserva = nombreReserva;
        this.reserva = reserva;
        this.habitacion = habitacion;
    }
}
