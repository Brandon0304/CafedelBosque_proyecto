package com.restaurante.patrones.chainofresponsibility;

import com.restaurante.gestionPedidos.model.Pedido;

/**
 * Manejador concreto: Admin (fallback)
 */
public class ManejadorAdmin extends ManejadorPedido {
    @Override
    public void manejar(Pedido pedido, String rol) {
        if ("ADMIN".equalsIgnoreCase(rol)) {
            System.out.println("👔 Admin: Revisando pedido #" + pedido.getId());
        } else {
            System.out.println("⚠️ Rol '" + rol + "' no tiene manejador asignado");
        }
    }
}

