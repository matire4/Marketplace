package com.uade.tpo.marketplace.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImagenDTO {
    private Long id;
    private byte[] imagen;
    private Long alojamientoId;
    private Long habitacionId;
}
