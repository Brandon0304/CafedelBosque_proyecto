package com.restaurante.patrones.factory;

import com.restaurante.gestionProductos.model.Producto;

import java.math.BigDecimal;

/**
 * Factory concreto: Productos calientes
 */
public class ProductoCalienteFactory extends ProductoFactory {
    @Override
    public Producto crearBebida() {
        Producto producto = new Producto();
        producto.setNombre("Café Caliente");
        producto.setCategoria("BEBIDA_CALIENTE");
        producto.setPrecio(new BigDecimal("5000"));
        producto.setDisponible(true);
        return producto;
    }

    @Override
    public Producto crearPlato() {
        Producto producto = new Producto();
        producto.setNombre("Sopa del Día");
        producto.setCategoria("PLATO_CALIENTE");
        producto.setPrecio(new BigDecimal("12000"));
        producto.setDisponible(true);
        return producto;
    }

    @Override
    public Producto crearPostre() {
        Producto producto = new Producto();
        producto.setNombre("Tarta Caliente de Chocolate");
        producto.setCategoria("POSTRE_CALIENTE");
        producto.setPrecio(new BigDecimal("8000"));
        producto.setDisponible(true);
        return producto;
    }
}

