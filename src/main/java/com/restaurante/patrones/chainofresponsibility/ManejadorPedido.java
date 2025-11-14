package com.restaurante.patrones.chainofresponsibility;

import com.restaurante.gestionPedidos.model.Pedido;

/**
 * Patrón Chain of Responsibility - Interfaz para manejadores de pedidos
 */
public abstract class ManejadorPedido {
    protected ManejadorPedido siguiente;

    public void establecerSiguiente(ManejadorPedido siguiente) {
        this.siguiente = siguiente;
    }

    public abstract void manejar(Pedido pedido, String rol);

    protected void pasarASiguiente(Pedido pedido, String rol) {
        if (siguiente != null) {
            siguiente.manejar(pedido, rol);
        }
    }
}

