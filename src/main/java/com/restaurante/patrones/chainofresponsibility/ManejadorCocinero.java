package com.restaurante.patrones.chainofresponsibility;

import com.restaurante.gestionPedidos.model.Pedido;

/**
 * Manejador concreto: Cocinero
 */
public class ManejadorCocinero extends ManejadorPedido {
    @Override
    public void manejar(Pedido pedido, String rol) {
        if ("COCINERO".equalsIgnoreCase(rol) || "BARISTA".equalsIgnoreCase(rol)) {
            System.out.println("👨‍🍳 Cocinero: Procesando pedido #" + pedido.getId());
            System.out.println("   Preparando " + pedido.getDetalles().size() + " item(s)...");
        } else {
            pasarASiguiente(pedido, rol);
        }
    }
}

