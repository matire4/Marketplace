package com.uade.tpo.marketplace.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "La habitacion que estas intentando crear ya existe")
public class HabitacionDuplicateException extends Exception{
    
}
