package com.uade.tpo.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entities.Hotel;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByEmail(String email);
    List<Hotel> findByUbicacion(String ubicacion);
    List<Hotel> findByCiudad(String ciudad);
    List<Hotel> findByCantidadViajeros(int cantidadViajeros);
    List<Hotel> findByDescuento(int descuento);
    List<Hotel> findByReviews(int reviews);
    List<Hotel> findByPreguntas(String preguntas);
}
