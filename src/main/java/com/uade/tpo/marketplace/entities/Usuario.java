package com.uade.tpo.marketplace.entities;

import java.util.List;

import com.uade.tpo.marketplace.enums.RolUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@DiscriminatorValue("usuario")
@Entity
public class Usuario extends Cuenta{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellido; 
    @Column(nullable = false)
    RolUsuario rolUsuario;

    @OneToMany(mappedBy = "usuario")
    private List<Reserva> reservas;
    @OneToMany(mappedBy = "usuario")
    private List<Review> reviews;

    @OneToOne
    private Carrito carrito;
}