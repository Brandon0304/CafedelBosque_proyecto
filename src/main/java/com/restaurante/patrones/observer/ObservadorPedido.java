package com.restaurante.patrones.observer;

import com.restaurante.gestionPedidos.model.Pedido;

/**
 * Patrón Observer - Interfaz para observadores de pedidos
 */
public interface ObservadorPedido {
    void actualizar(Pedido pedido, String estado);
    String getRol(); // Para identificar qué tipo de empleado es
}

