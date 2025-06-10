package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Alojamiento no encontrado")
public class AlojamientoNotFoundException extends Exception {
    public AlojamientoNotFoundException(String message) {
        super(message);
    }
}