package com.restaurante.patrones.command;

import com.restaurante.gestionPedidos.model.Pedido;
import com.restaurante.patrones.mediator.MediadorRestaurante;

/**
 * Command concreto: Enviar pedido al cocinero
 */
public class ComandoEnviarACocinero implements ComandoPedido {
    private final Pedido pedido;
    private final MediadorRestaurante mediador;

    public ComandoEnviarACocinero(Pedido pedido, MediadorRestaurante mediador) {
        this.pedido = pedido;
        this.mediador = mediador;
    }

    @Override
    public void ejecutar() {
        System.out.println("Enviando pedido #" + pedido.getId() + " al cocinero...");
        mediador.enviarPedidoACocinero(pedido);
    }

    @Override
    public void deshacer() {
        System.out.println("Cancelando envío de pedido al cocinero");
    }

    @Override
    public Pedido obtenerPedido() {
        return pedido;
    }
}

