package com.uade.tpo.marketplace.entities.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartamentoDTO {
    private Long id;
    private int capacidad;
    private double precioPorNoche;
    private String numeroDepartamento;
    private String descripcion;
    private int ambientes;
    private int banos;  
    private int dormitorios;
    private int camas;
    private String breveDescripcion;
    private String direccion;
    private String ciudad;
    private String pais;
    
    // Solo IDs de imágenes en el response, no las imágenes completas
    private List<Long> imagenesIds;
    
    // Solo para recibir archivos en el request
    private List<MultipartFile> imagenesNuevas;
    
    private String username;
    private String categoria;
}