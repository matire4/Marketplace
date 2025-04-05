package com.uade.tpo.marketplace.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@DiscriminatorValue("admin")
@Data
public class Gestor extends Cuenta{
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false,unique = true)
    private String cuil;
    @OneToMany(mappedBy = "gestor")
    private List<Hotel> hoteles;
}
