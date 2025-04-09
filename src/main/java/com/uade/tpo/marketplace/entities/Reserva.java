package com.uade.tpo.marketplace.entities;

import java.sql.Date;
import java.util.List;

import com.uade.tpo.marketplace.enums.Estado;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date fecha;
    @Column(nullable = false)
    private double precio;
    @Column(nullable = false)
    Estado estado;

    // En Reserva.java
    @ManyToMany
    @JoinTable(name = "habitacion_reserva", joinColumns = @JoinColumn(name = "reserva_id"), inverseJoinColumns = @JoinColumn(name = "habitacion_id"))
    private List<Habitacion> habitaciones;
    @ManyToOne
    @JoinColumn(name = "usuario_id") // esta columna va a estar en la tabla Reserva
    private Usuario usuario;

    public Reserva() {
    }

    public Reserva(Date fecha, double precio, Estado estado) {
        this.fecha = fecha;
        this.precio = precio;
        this.estado = estado;
    }
}
