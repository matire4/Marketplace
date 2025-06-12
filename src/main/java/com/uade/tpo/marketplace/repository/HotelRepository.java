package com.uade.tpo.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entities.Hotel;
import java.util.List;
import java.util.Optional;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByEmail(String email);
    List<Hotel> findByNombre(String nombre);
    Optional<Hotel> findByNombreAndDireccion(String nombre, String direccion);
    boolean existsByNombreAndDireccion(String nombre, String direccion);
}
