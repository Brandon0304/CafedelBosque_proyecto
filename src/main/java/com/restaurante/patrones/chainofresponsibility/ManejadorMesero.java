package com.restaurante.patrones.chainofresponsibility;

import com.restaurante.gestionPedidos.model.Pedido;

/**
 * Manejador concreto: Mesero
 */
public class ManejadorMesero extends ManejadorPedido {
    @Override
    public void manejar(Pedido pedido, String rol) {
        if ("MESERO".equalsIgnoreCase(rol)) {
            System.out.println("👤 Mesero: Atendiendo pedido #" + pedido.getId());
            System.out.println("   Cliente: " + pedido.getNombreCliente());
            System.out.println("   Total: $" + pedido.getTotal());
        } else {
            pasarASiguiente(pedido, rol);
        }
    }
}

