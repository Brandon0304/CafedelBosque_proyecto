package com.restaurante.patrones.builder;

import com.restaurante.gestionProductos.model.Producto;
import com.restaurante.patrones.command.ComandoPedido;

import java.util.ArrayList;
import java.util.List;

/**
 * Patrón Builder - Construye el menú paso a paso
 */
public class MenuBuilder {
    private final List<Producto> productos = new ArrayList<>();
    private final List<ComandoPedido> comandos = new ArrayList<>();

    public MenuBuilder agregarBebida(Producto bebida) {
        productos.add(bebida);
        return this;
    }

    public MenuBuilder agregarPlato(Producto plato) {
        productos.add(plato);
        return this;
    }

    public MenuBuilder agregarPostre(Producto postre) {
        productos.add(postre);
        return this;
    }

    public MenuBuilder agregarComando(ComandoPedido comando) {
        comandos.add(comando);
        return this;
    }

    public Menu construir() {
        return new Menu(productos, comandos);
    }
}

