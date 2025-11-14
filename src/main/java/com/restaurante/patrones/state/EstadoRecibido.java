package com.restaurante.patrones.state;

/**
 * Estado inicial: Pedido recibido
 */
public class EstadoRecibido implements EstadoPedido {
    @Override
    public void procesar() {
        System.out.println("Pedido recibido, listo para procesar");
    }

    @Override
    public void cocinar() {
        System.out.println("Iniciando cocción del pedido...");
    }

    @Override
    public void terminar() {
        System.out.println("El pedido debe estar en cocción antes de terminar");
    }

    @Override
    public void cancelar() {
        System.out.println("Pedido cancelado");
    }

    @Override
    public String obtenerEstado() {
        return "RECIBIDO";
    }
}

