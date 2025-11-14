package com.restaurante.patrones.state;

/**
 * Patrón State - Define el estado de un pedido
 */
public interface EstadoPedido {
    void procesar();
    void cocinar();
    void terminar();
    void cancelar();
    String obtenerEstado();
}

