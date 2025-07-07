package com.uade.tpo.marketplace.controllers;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entities.Imagen;
import com.uade.tpo.marketplace.entities.dto.ImagenDTO;
import com.uade.tpo.marketplace.service.ImagenService;

@RestController
@RequestMapping("/api/v1/imagenes")
public class ImagenController {
    
    @Autowired
    private ImagenService imagenService;

    @GetMapping("/{imagenId}")
    public ResponseEntity<byte[]> getImagen(@PathVariable Long imagenId) {
        try {
            Optional<Imagen> imagenOpt = imagenService.getImagenById(imagenId);
            
            if (imagenOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Imagen imagen = imagenOpt.get();
            
            // Decodificar la imagen Base64 a bytes
            byte[] imageBytes = Base64.getDecoder().decode(imagen.getImagen());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("image/jpeg")); // Ajusta según el tipo de imagen
            headers.setContentLength(imageBytes.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(imageBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/alojamiento/{alojamientoId}")
    public ResponseEntity<List<ImagenDTO>> getImagenesByAlojamiento(@PathVariable Long alojamientoId) {
        try {
            List<ImagenDTO> imagenes = imagenService.getImagenesByAlojamientoId(alojamientoId);
            return ResponseEntity.ok(imagenes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/alojamiento/{alojamientoId}/ids")
    public ResponseEntity<List<Long>> getImagenesIdsByAlojamiento(@PathVariable Long alojamientoId) {
        try {
            List<ImagenDTO> imagenes = imagenService.getImagenesByAlojamientoId(alojamientoId);
            List<Long> imagenesIds = imagenes.stream().map(ImagenDTO::getId).toList();
            return ResponseEntity.ok(imagenesIds);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/habitacion/{habitacionId}")
    public ResponseEntity<List<ImagenDTO>> getImagenesByHabitacion(@PathVariable Long habitacionId) {
        try {
            List<ImagenDTO> imagenes = imagenService.getImagenesByHabitacionId(habitacionId);
            return ResponseEntity.ok(imagenes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/habitacion/{habitacionId}/ids")
    public ResponseEntity<List<Long>> getImagenesIdsByHabitacion(@PathVariable Long habitacionId) {
        try {
            List<ImagenDTO> imagenes = imagenService.getImagenesByHabitacionId(habitacionId);
            List<Long> imagenesIds = imagenes.stream().map(ImagenDTO::getId).toList();
            return ResponseEntity.ok(imagenesIds);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
