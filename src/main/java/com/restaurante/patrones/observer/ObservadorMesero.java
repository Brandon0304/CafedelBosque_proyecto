package com.restaurante.patrones.observer;

import com.restaurante.gestionPedidos.model.Pedido;

/**
 * Observador concreto: Mesero
 */
public class ObservadorMesero implements ObservadorPedido {
    private final String nombreMesero;

    public ObservadorMesero(String nombreMesero) {
        this.nombreMesero = nombreMesero;
    }

    @Override
    public void actualizar(Pedido pedido, String estado) {
        System.out.println("🔔 NOTIFICACIÓN para Mesero " + nombreMesero + 
                         ": El pedido #" + pedido.getId() + " cambió a estado: " + estado);
    }

    @Override
    public String getRol() {
        return "MESERO";
    }
}

