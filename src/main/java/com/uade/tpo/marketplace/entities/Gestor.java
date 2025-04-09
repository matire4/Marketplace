package com.uade.tpo.marketplace.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("gestor")
@EqualsAndHashCode(callSuper = true)
@Data
public class Gestor extends Cuenta {
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false, unique = true)
    private String cuil;
    @OneToMany(mappedBy = "gestor")
    private List<Hotel> hoteles;

    public Gestor() {
    }

    public Gestor(String username, String password, String email, String telefono, String nombre, String cuil) {
        super(username, password, email, telefono);
        this.nombre = nombre;
        this.cuil = cuil;
    }
}
