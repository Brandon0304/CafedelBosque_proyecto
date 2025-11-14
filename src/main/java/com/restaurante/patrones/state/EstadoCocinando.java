package com.restaurante.patrones.state;

/**
 * Estado: Pedido en cocción
 */
public class EstadoCocinando implements EstadoPedido {
    @Override
    public void procesar() {
        System.out.println("El pedido ya está siendo procesado (en cocción)");
    }

    @Override
    public void cocinar() {
        System.out.println("El pedido ya está en cocción");
    }

    @Override
    public void terminar() {
        System.out.println("Terminando pedido...");
    }

    @Override
    public void cancelar() {
        System.out.println("Cancelando pedido en cocción");
    }

    @Override
    public String obtenerEstado() {
        return "COCINANDO";
    }
}

