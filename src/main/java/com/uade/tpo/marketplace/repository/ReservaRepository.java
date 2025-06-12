package com.uade.tpo.marketplace.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entities.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByUsuarioId(Long usuarioId);

    List<Reserva> findByFecha(Date fecha);

    List<Reserva> findByPrecio(double precio);
}
