package com.uade.tpo.marketplace.entities;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "carrito_habitacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoHabitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreReserva;

    @Column(nullable = false)
    private int cantidad;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date checkIn;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date checkOut;

    @Column(nullable = false)
    private Double precio;

    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;
}
