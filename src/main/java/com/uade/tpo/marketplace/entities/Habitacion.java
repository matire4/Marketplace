package com.uade.tpo.marketplace.entities;

import java.util.List;

import com.uade.tpo.marketplace.enums.TipoHabitacion;

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
public class Habitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    TipoHabitacion tipoHabitacion;
    @Column(nullable = false)
    private int ambientes;
    @Column(nullable = false)
    private int banos;
    @Column(nullable = false)
    private int dormitorios;
    @Column(nullable = false)
    private int camas;
    @Column(nullable = false)  
    int capacidad;
    @Column(nullable = false)
    double precioPorNoche;
    @Column(nullable = false)
    String numeroHabitacion;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservaHabitacion> reservasHabitacion;
    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoHabitacion> carritoHabitacion;
    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagen> imagenesHabitacion;


    @ManyToOne
    @JoinColumn(name = "gestor_id", nullable = false)
    private Gestor gestor;
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    public Habitacion() {
    }

    public Habitacion(TipoHabitacion tipoHabitacion, int capacidad, double precioPorNoche, String numeroHabitacion,
            List<Imagen> imagenesHabitacion, Gestor gestor, Categoria categoria) {
        this.tipoHabitacion = tipoHabitacion;
        this.capacidad = capacidad;
        this.precioPorNoche = precioPorNoche;
        this.numeroHabitacion = numeroHabitacion;
        this.imagenesHabitacion = imagenesHabitacion;
        this.gestor = gestor;
        this.categoria = categoria;
    }
}
