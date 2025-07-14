package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.entities.CarritoDepartamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface CarritoDepartamentoRepository extends JpaRepository<CarritoDepartamento, Long> {
    List<CarritoDepartamento> findByCarritoId(Long carritoId);
    
    boolean existsByCarritoIdAndDepartamentoId(Long carritoId, Long departamentoId);
    List<CarritoDepartamento> findByHabitacionId(Long habitacionId);
    List<CarritoDepartamento> findByUsuario(String username);
    List<CarritoDepartamento> findByDepartamentoId(Long departamentoId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CarritoDepartamento cd WHERE cd.carrito.id = :carritoId AND cd.departamento.id = :departamentoId")
    void deleteByCarritoIdAndDepartamentoId(@Param("carritoId") Long carritoId, @Param("departamentoId") Long departamentoId);
}
