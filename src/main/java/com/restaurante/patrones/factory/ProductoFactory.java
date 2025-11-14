package com.restaurante.patrones.factory;

import com.restaurante.gestionProductos.model.Producto;

import java.math.BigDecimal;

/**
 * Patrón Abstract Factory - Crea familias de productos
 */
public abstract class ProductoFactory {
    public abstract Producto crearBebida();
    public abstract Producto crearPlato();
    public abstract Producto crearPostre();
}

