package com.uade.tpo.marketplace.entities;

import java.sql.Date;

import com.uade.tpo.marketplace.enums.Estado;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    @ManyToMany(mappedBy = "reserva")
    @JoinTable(name = "habitacion_reserva",joinColumns = @JoinColumn(name = "habitacion_id"), inverseJoinColumns = @JoinColumn(name = "reserva_id"))
    private Habitacion habitacion;
}
