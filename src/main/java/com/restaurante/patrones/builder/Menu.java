package com.restaurante.patrones.builder;

import com.restaurante.gestionProductos.model.Producto;
import com.restaurante.patrones.command.ComandoPedido;

import java.util.List;

/**
 * Producto final del Builder
 */
public class Menu {
    private final List<Producto> productos;
    private final List<ComandoPedido> comandos;

    public Menu(List<Producto> productos, List<ComandoPedido> comandos) {
        this.productos = productos;
        this.comandos = comandos;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public List<ComandoPedido> getComandos() {
        return comandos;
    }

    public void mostrarMenu() {
        System.out.println("=== MENÚ DEL DÍA ===");
        productos.forEach(p -> 
            System.out.println("- " + p.getNombre() + " ($" + p.getPrecio() + ")")
        );
    }
}

