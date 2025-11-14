package com.restaurante.patrones.state;

/**
 * Estado final: Pedido terminado
 */
public class EstadoTerminado implements EstadoPedido {
    @Override
    public void procesar() {
        System.out.println("El pedido ya fue terminado");
    }

    @Override
    public void cocinar() {
        System.out.println("El pedido ya fue terminado, no puede volver a cocción");
    }

    @Override
    public void terminar() {
        System.out.println("El pedido ya está terminado");
    }

    @Override
    public void cancelar() {
        System.out.println("No se puede cancelar un pedido ya terminado");
    }

    @Override
    public String obtenerEstado() {
        return "TERMINADO";
    }
}

