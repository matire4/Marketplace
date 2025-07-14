package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Categoria no encontrado")
public class CategoriaNotFoundException extends Exception {
    public CategoriaNotFoundException() {
        super();
    }
    
    public CategoriaNotFoundException(String message) {
        super(message);
    }
}
