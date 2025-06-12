package com.uade.tpo.marketplace.controllers;
import com.uade.tpo.marketplace.service.HotelbedsIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotelbeds")
public class HotelbedsController {

    @Autowired
    private HotelbedsIntegrationService hotelbedsIntegrationService;

    /**
     * Endpoint para consumir hoteles desde Hotelbeds y guardarlos en la base.
     * @param from índice inicial (ej: 1)
     * @param to índice final (ej: 10)
     * @param gestorId ID del gestor al que se asignarán los hoteles
     * @param categoriaId ID de la categoría que tendrán los hoteles
     */
    @PostMapping("/import")
    public String importarHoteles(
            @RequestParam int from,
            @RequestParam int to,
            @RequestParam Long gestorId,
            @RequestParam Long categoriaId
    ) {
        hotelbedsIntegrationService.fetchAndPersistHotels(from, to, gestorId, categoriaId);
        return "Importación de hoteles en progreso.";
    }
}
