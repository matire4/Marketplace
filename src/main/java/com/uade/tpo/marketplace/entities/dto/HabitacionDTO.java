package com.uade.tpo.marketplace.entities.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.marketplace.enums.TipoHabitacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitacionDTO {
    private Long id;
    private TipoHabitacion tipoHabitacion;
    private int capacidad;
    private double precioPorNoche;
    private String numeroHabitacion;
    private int ambientes;
    private int banos;
    private int dormitorios;
    private int camas;
    
    // Solo IDs de imágenes en el response, no las imágenes completas
    private List<Long> imagenesIds;
    
    // Solo para recibir archivos en el request
    private List<MultipartFile> imagenesNuevas;
    
    private String hotel;
    private List<ReservaHabitacionDTO> reservas;
    private List<CarritoHabitacionDTO> carritos;
    private String gestor;
    private String categoria;
}