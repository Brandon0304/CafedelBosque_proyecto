package com.restaurante.patrones.mediator;

import com.restaurante.gestionPedidos.model.Pedido;
import com.restaurante.patrones.observer.ObservadorMesero;
import com.restaurante.patrones.observer.SujetoPedido;
import org.springframework.stereotype.Component;

/**
 * Patrón Mediator - Intermedia entre cliente y cocinero
 */
@Component
public class MediadorRestaurante {
    private final SujetoPedido sujetoPedido;

    public MediadorRestaurante() {
        this.sujetoPedido = new SujetoPedido();
    }

    public void enviarPedidoACocinero(Pedido pedido) {
        sujetoPedido.setPedido(pedido);
        System.out.println("📋 Mediador: Recibiendo pedido #" + pedido.getId() + " del cliente");
        System.out.println("👨‍🍳 Mediador: Enviando pedido al cocinero...");
        sujetoPedido.notificarObservadores("COCINANDO");
    }

    public void pedidoListo(Pedido pedido) {
        sujetoPedido.setPedido(pedido);
        System.out.println("✅ Mediador: Pedido #" + pedido.getId() + " terminado, notificando mesero...");
        sujetoPedido.notificarObservadores("TERMINADO");
    }

    public void registrarMesero(String nombreMesero) {
        sujetoPedido.agregarObservador(new ObservadorMesero(nombreMesero));
    }
}

