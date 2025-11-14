package com.restaurante.patrones.decorator;

import com.restaurante.gestionProductos.model.Producto;

import java.math.BigDecimal;

/**
 * Decorator concreto: Aplica descuento a un producto
 */
public class ProductoConDescuento extends ProductoDecorator {
    private final double porcentajeDescuento;

    public ProductoConDescuento(Producto producto, double porcentajeDescuento) {
        super(producto);
        this.porcentajeDescuento = porcentajeDescuento;
    }

    @Override
    public BigDecimal getPrecio() {
        BigDecimal precioOriginal = producto.getPrecio();
        BigDecimal descuento = precioOriginal.multiply(
            BigDecimal.valueOf(porcentajeDescuento / 100.0)
        );
        return precioOriginal.subtract(descuento);
    }

    public String getNombre() {
        return producto.getNombre() + " (con " + porcentajeDescuento + "% descuento)";
    }
}

