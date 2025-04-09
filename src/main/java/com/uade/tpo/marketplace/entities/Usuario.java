package com.uade.tpo.marketplace.entities;

import java.util.List;

import com.uade.tpo.marketplace.enums.RolUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@DiscriminatorValue("usuario")
@EqualsAndHashCode(callSuper = true)
@Entity
public class Usuario extends Cuenta {
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

    public Usuario() {
    }

    public Usuario(String nombre, String apellido, RolUsuario rolUsuario, String username, String password,
            String email, String telefono) {
        super(username, password, email, telefono);
        this.nombre = nombre;
        this.apellido = apellido;
        this.rolUsuario = rolUsuario;
    }
}