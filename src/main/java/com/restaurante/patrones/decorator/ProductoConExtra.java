package com.restaurante.patrones.decorator;

import com.restaurante.gestionProductos.model.Producto;

import java.math.BigDecimal;

/**
 * Decorator concreto: Agrega extras a un producto
 */
public class ProductoConExtra extends ProductoDecorator {
    private final String extra;
    private final BigDecimal precioExtra;

    public ProductoConExtra(Producto producto, String extra, BigDecimal precioExtra) {
        super(producto);
        this.extra = extra;
        this.precioExtra = precioExtra;
    }

    @Override
    public BigDecimal getPrecio() {
        return producto.getPrecio().add(precioExtra);
    }

    public String getNombre() {
        return producto.getNombre() + " con " + extra;
    }
}

