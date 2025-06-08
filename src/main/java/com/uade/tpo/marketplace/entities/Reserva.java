package com.uade.tpo.marketplace.entities;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    // En Reserva.java
    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservaHabitacion> ReservasHabitacion;
    @ManyToOne
    @JoinColumn(name = "usuario_id") // esta columna va a estar en la tabla Reserva
    private Usuario usuario;

    public Reserva() {
    }

    public Reserva(Date fecha, List<ReservaHabitacion> ReservasHabitacion, Usuario usuario) {
        this.fecha = fecha;
        this.ReservasHabitacion = ReservasHabitacion;
        this.usuario = usuario;
        this.precio = ReservasHabitacion.stream().mapToDouble(ReservaHabitacion::getPrecio).sum();
    }
}
