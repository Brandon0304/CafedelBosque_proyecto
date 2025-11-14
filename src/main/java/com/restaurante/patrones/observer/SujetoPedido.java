package com.restaurante.patrones.observer;

import com.restaurante.gestionPedidos.model.Pedido;

import java.util.ArrayList;
import java.util.List;

/**
 * Sujeto observable - Notifica cambios de estado de pedidos
 */
public class SujetoPedido {
    private final List<ObservadorPedido> observadores = new ArrayList<>();
    private Pedido pedido;
    private String estadoActual;

    public void agregarObservador(ObservadorPedido observador) {
        observadores.add(observador);
    }

    public void removerObservador(ObservadorPedido observador) {
        observadores.remove(observador);
    }

    public void notificarObservadores(String nuevoEstado) {
        this.estadoActual = nuevoEstado;
        for (ObservadorPedido observador : observadores) {
            observador.actualizar(pedido, nuevoEstado);
        }
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}

