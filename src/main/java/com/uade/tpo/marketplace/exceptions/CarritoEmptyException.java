package com.uade.tpo.marketplace.exceptions;

public class CarritoEmptyException extends Exception {
    public CarritoEmptyException() {
        super("El carrito está vacío");
    }
}