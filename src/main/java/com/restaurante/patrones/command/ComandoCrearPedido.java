package com.restaurante.patrones.command;

import com.restaurante.gestionPedidos.model.Pedido;
import com.restaurante.gestionPedidos.repository.PedidoRepository;

/**
 * Command concreto: Crear pedido
 */
public class ComandoCrearPedido implements ComandoPedido {
    private final PedidoRepository pedidoRepository;
    private final String nombreCliente;
    private Pedido pedidoCreado;

    public ComandoCrearPedido(PedidoRepository pedidoRepository, String nombreCliente) {
        this.pedidoRepository = pedidoRepository;
        this.nombreCliente = nombreCliente;
    }

    @Override
    public void ejecutar() {
        Pedido pedido = new Pedido();
        pedido.setNombreCliente(nombreCliente);
        this.pedidoCreado = pedidoRepository.save(pedido);
        System.out.println("Pedido creado con ID: " + pedidoCreado.getId());
    }

    @Override
    public void deshacer() {
        if (pedidoCreado != null && pedidoCreado.getId() != null) {
            pedidoRepository.deleteById(pedidoCreado.getId());
            System.out.println("Pedido eliminado (deshacer)");
        }
    }

    @Override
    public Pedido obtenerPedido() {
        return pedidoCreado;
    }
}

