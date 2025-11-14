package com.restaurante.patrones.memento;

import com.restaurante.gestionPedidos.model.Pedido;

import java.time.LocalDateTime;

/**
 * Patrón Memento - Guarda el estado de un pedido
 */
public class PedidoMemento {
    private final Long id;
    private final String nombreCliente;
    private final LocalDateTime fechaHora;
    private final boolean pagado;
    private final int cantidadDetalles;
    private final double total;

    public PedidoMemento(Pedido pedido) {
        this.id = pedido.getId();
        this.nombreCliente = pedido.getNombreCliente();
        this.fechaHora = pedido.getFechaHora();
        this.pagado = pedido.isPagado();
        this.cantidadDetalles = pedido.getDetalles().size();
        this.total = pedido.getTotal();
    }

    public Long getId() {
        return id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public boolean isPagado() {
        return pagado;
    }

    public int getCantidadDetalles() {
        return cantidadDetalles;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return String.format("PedidoMemento{id=%d, cliente='%s', fecha=%s, total=$%.2f, items=%d}",
                id, nombreCliente, fechaHora, total, cantidadDetalles);
    }
}

