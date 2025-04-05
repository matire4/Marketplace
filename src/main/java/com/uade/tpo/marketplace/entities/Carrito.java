package com.uade.tpo.marketplace.entities;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinTable;

@Data
@Entity
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "UsuarioId", referencedColumnName = "UsuarioId")
    private Usuario usuario;
    @ManyToMany
    @JoinTable(name = "carrito_habitacion", joinColumns = @JoinColumn(name = "carrito_id"), inverseJoinColumns = @JoinColumn(name = "habitacion_id"))
    private Habitacion habitacion;
}
