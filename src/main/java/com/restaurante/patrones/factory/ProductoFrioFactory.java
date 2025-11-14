package com.restaurante.patrones.factory;

import com.restaurante.gestionProductos.model.Producto;

import java.math.BigDecimal;

/**
 * Factory concreto: Productos fríos
 */
public class ProductoFrioFactory extends ProductoFactory {
    @Override
    public Producto crearBebida() {
        Producto producto = new Producto();
        producto.setNombre("Limonada");
        producto.setCategoria("BEBIDA_FRIA");
        producto.setPrecio(new BigDecimal("6000"));
        producto.setDisponible(true);
        return producto;
    }

    @Override
    public Producto crearPlato() {
        Producto producto = new Producto();
        producto.setNombre("Ensalada César");
        producto.setCategoria("PLATO_FRIO");
        producto.setPrecio(new BigDecimal("15000"));
        producto.setDisponible(true);
        return producto;
    }

    @Override
    public Producto crearPostre() {
        Producto producto = new Producto();
        producto.setNombre("Helado de Vainilla");
        producto.setCategoria("POSTRE_FRIO");
        producto.setPrecio(new BigDecimal("7000"));
        producto.setDisponible(true);
        return producto;
    }
}

