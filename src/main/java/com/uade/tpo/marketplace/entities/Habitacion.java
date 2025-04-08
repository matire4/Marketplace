package com.uade.tpo.marketplace.entities;

import java.util.List;

import com.uade.tpo.marketplace.enums.TipoHabitacion;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

public class Habitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    TipoHabitacion tipoHabitacion;
    @Column
    int capacidad;
    @Column
    double precioPorNoche;
    @Column
    String numeroHabitacion;
    @Column
    String imagen;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @ManyToMany(mappedBy = "habitaciones")
    private List<Reserva> reservas;
    @ManyToMany(mappedBy = "habitaciones")
    private List<Carrito> carritos;

    public Habitacion() {
    }

    public Habitacion(TipoHabitacion tipoHabitacion, int capacidad, double precioPorNoche, String numeroHatiacion,
            String imagen) {
        this.tipoHabitacion = tipoHabitacion;
        this.capacidad = capacidad;
        this.precioPorNoche = precioPorNoche;
        this.numeroHabitacion = numeroHatiacion;
        this.imagen = imagen;
    }
}
