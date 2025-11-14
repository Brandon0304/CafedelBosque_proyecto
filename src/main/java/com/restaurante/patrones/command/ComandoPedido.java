package com.restaurante.patrones.command;

import com.restaurante.gestionPedidos.model.Pedido;

/**
 * Patrón Command - Interfaz para comandos de pedidos
 */
public interface ComandoPedido {
    void ejecutar();
    void deshacer();
    Pedido obtenerPedido();
}

