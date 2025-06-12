package com.uade.tpo.marketplace.entities;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.uade.tpo.marketplace.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    Role role;

    @OneToMany(mappedBy = "usuario")
    private List<Reserva> reservas = new ArrayList<>();
    @OneToMany(mappedBy = "usuario")
    private List<Review> reviews = new ArrayList<>();

    @OneToOne
    private Carrito carrito;

    public Usuario() {
    }

    public Usuario(String nombre, String apellido, Role rolUsuario, String username, String password,
            String email, String telefono) {
        super(username, password, email, telefono);
        this.nombre = nombre;
        this.apellido = apellido;
        this.role = rolUsuario;
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