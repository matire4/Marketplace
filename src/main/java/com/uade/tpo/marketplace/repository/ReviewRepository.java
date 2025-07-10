package com.uade.tpo.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entities.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByAlojamientoId(Long alojamientoId);
    
    List<Review> findByUsuarioId(Long usuarioId);
    
    @Query("SELECT r FROM Review r WHERE r.usuario.username = :username")
    List<Review> findByUsuarioUsername(@Param("username") String username);
    
    @Query("SELECT r FROM Review r WHERE r.alojamiento.id = :alojamientoId AND r.usuario.username = :username")
    List<Review> findByAlojamientoIdAndUsuarioUsername(@Param("alojamientoId") Long alojamientoId, @Param("username") String username);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.alojamiento.id = :alojamientoId")
    Double findAverageRatingByAlojamientoId(@Param("alojamientoId") Long alojamientoId);
}
