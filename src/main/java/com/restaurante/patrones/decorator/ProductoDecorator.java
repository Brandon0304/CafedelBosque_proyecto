package com.restaurante.patrones.decorator;

import com.restaurante.gestionProductos.model.Producto;

import java.math.BigDecimal;

/**
 * Patrón Decorator - Clase base para decoradores de productos
 */
public abstract class ProductoDecorator {
    protected Producto producto;

    public ProductoDecorator(Producto producto) {
        this.producto = producto;
    }

    public String getNombre() {
        return producto.getNombre();
    }

    public String getCategoria() {
        return producto.getCategoria();
    }
    
    public abstract BigDecimal getPrecio();
    
    public boolean isDisponible() {
        return producto.isDisponible();
    }
    
    public Long getId() {
        return producto.getId();
    }
}

