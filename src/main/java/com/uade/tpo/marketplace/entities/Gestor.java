package com.uade.tpo.marketplace.entities;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import com.uade.tpo.marketplace.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("gestor")
@EqualsAndHashCode(callSuper = true)
@Data
public class Gestor extends Cuenta{
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false, unique = true)
    private String cuil;
    @Enumerated(EnumType.STRING)
    Role role = Role.GESTOR; // for difference between gestor and user
    @OneToMany(mappedBy = "gestor")
    private List<Hotel> hoteles;
    @OneToMany(mappedBy = "gestor")
    private List<Departamento> departamentos;

    public Gestor() {
    }

    public Gestor(String username, String password, String email, String telefono, String nombre, String cuil) {
        super(username, password, email, telefono);
        this.nombre = nombre;
        this.cuil = cuil;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
